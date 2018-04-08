/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.krillsson.sysapi.auth.BasicAuthenticator;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMixin;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import com.krillsson.sysapi.core.metrics.MetricsProvider;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.util.EnvironmentUtils;
import com.krillsson.sysapi.util.Utils;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.sslreload.SslReloadBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.net.NetworkInterface;
import java.time.Clock;
import java.util.concurrent.Executors;


public class SystemApiApplication extends Application<SystemApiConfiguration> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SystemApiApplication.class);
    private Environment environment;

    public static void main(String[] args) throws Exception {
        new SystemApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "System-Api";
    }

    @Override
    public void initialize(Bootstrap<SystemApiConfiguration> bootstrap) {
        ObjectMapper mapper = bootstrap.getObjectMapper();
        mapper.addMixInAnnotations(NetworkInterface.class, NetworkInterfaceMixin.class);
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter(
                        "networkInterface filter",
                        SimpleBeanPropertyFilter.serializeAllExcept(
                                "name",
                                "displayName",
                                "inetAddresses",
                                "interfaceAddresses",
                                "mtu",
                                "subInterfaces"
                        )
                );
        mapper.setFilters(filterProvider);
        bootstrap.addBundle(new SslReloadBundle());
    }

    @Override
    public void run(SystemApiConfiguration config, Environment environment) throws Exception {
        this.environment = environment;

        if (config.forwardHttps()) {
            EnvironmentUtils.addHttpsForward(environment.getApplicationContext());
        }
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        final BasicCredentialAuthFilter<UserConfiguration> userBasicCredentialAuthFilter =
                new BasicCredentialAuthFilter.Builder<UserConfiguration>()
                        .setAuthenticator(new BasicAuthenticator(config.getUser()))
                        .setRealm("System-Api")
                        .setAuthorizer(new BasicAuthorizer(config.getUser()))
                        .buildAuthFilter();

        SystemInfo systemInfo = new SystemInfo();

        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        environment.jersey().register(new AuthDynamicFeature(userBasicCredentialAuthFilter));
        environment.jersey().register(new AuthValueFactoryProvider.Binder(UserConfiguration.class));

        SpeedMeasurementManager speedMeasurementManager = new SpeedMeasurementManager(
                Executors.newScheduledThreadPool(
                        2,
                        new ThreadFactoryBuilder()
                                .setNameFormat("speed-mgr-%d")
                                .build()
                ), Clock.systemUTC(), 5);

        MetricsFactory provider = new MetricsProvider(
                hal,
                os,
                SystemInfo.getCurrentPlatformEnum(),
                config,
                speedMeasurementManager
        ).create();
        environment.lifecycle().manage(speedMeasurementManager);

        MetricsHistoryManager historyManager = new MetricsHistoryManager(
                Executors.newSingleThreadScheduledExecutor(
                        new ThreadFactoryBuilder()
                                .setNameFormat("history-mgr-%d")
                                .build()), historyConfiguration)
                .initializeWith(provider);
        environment.lifecycle().manage(historyManager);

        environment.jersey().register(new SystemResource(
                SystemInfo.getCurrentPlatformEnum(),
                provider.cpuMetrics(),
                provider.networkMetrics(),
                provider.driveMetrics(),
                provider.memoryMetrics(),
                provider.gpuMetrics(),
                provider.motherboardMetrics(),
                historyManager, () -> hal.getProcessor().getSystemUptime()
        ));
        environment.jersey().register(new DrivesResource(provider.driveMetrics(), historyManager));
        environment.jersey().register(new GpuResource(provider.gpuMetrics(), historyManager));
        environment.jersey().register(new MemoryResource(provider.memoryMetrics(), historyManager));
        environment.jersey().register(new NetworkInterfacesResource(provider.networkMetrics(), historyManager));
        environment.jersey().register(new ProcessesResource(provider.processesMetrics()));
        environment.jersey().register(new CpuResource(provider.cpuMetrics(), historyManager));
        environment.jersey().register(new MotherboardResource(provider.motherboardMetrics()));
        environment.jersey().register(
                new MetaInfoResource(
                        Utils.getVersionFromManifest(),
                        EnvironmentUtils.getEndpoints(environment),
                        os.getProcessId()
                ));
    }


    public Environment getEnvironment() {
        return environment;
    }

}
