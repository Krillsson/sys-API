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
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.krillsson.sysapi.auth.BasicAuthenticator;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.TickManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMixin;
import com.krillsson.sysapi.core.domain.system.SystemLoad;
import com.krillsson.sysapi.core.history.HistoryMetricQueryEvent;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.MetricsFactory;
import com.krillsson.sysapi.core.metrics.MetricsProvider;
import com.krillsson.sysapi.core.monitoring.MonitorManager;
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent;
import com.krillsson.sysapi.core.query.MetricQueryManager;
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager;
import com.krillsson.sysapi.dto.monitor.Monitor;
import com.krillsson.sysapi.persistence.JsonFile;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.util.EnvironmentUtils;
import com.krillsson.sysapi.util.LocalDateTimeConverter;
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
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


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
        EventBus eventBus = new EventBus();
        final BasicCredentialAuthFilter<UserConfiguration> userBasicCredentialAuthFilter =
                new BasicCredentialAuthFilter.Builder<UserConfiguration>()
                        .setAuthenticator(new BasicAuthenticator(config.user()))
                        .setRealm("System-Api")
                        .setAuthorizer(new BasicAuthorizer(config.user()))
                        .buildAuthFilter();

        SystemInfo systemInfo = new SystemInfo();

        HardwareAbstractionLayer hal = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        environment.jersey().register(LocalDateTimeConverter.class);
        environment.jersey().register(new AuthDynamicFeature(userBasicCredentialAuthFilter));
        environment.jersey().register(new AuthValueFactoryProvider.Binder(UserConfiguration.class));
        TickManager tickManager = new TickManager(Executors.newScheduledThreadPool(
                1,
                new ThreadFactoryBuilder()
                        .setNameFormat("tick-mgr-%d")
                        .build()
        ), 5);
        SpeedMeasurementManager speedMeasurementManager = new SpeedMeasurementManager(
                Executors.newScheduledThreadPool(
                        1,
                        new ThreadFactoryBuilder()
                                .setNameFormat("speed-mgr-%d")
                                .build()
                ), Clock.systemUTC(), 5);

        MetricsFactory provider = new MetricsProvider(
                hal,
                os,
                SystemInfo.getCurrentPlatformEnum(),
                config,
                speedMeasurementManager,
                tickManager
        ).create();
        environment.lifecycle().manage(speedMeasurementManager);
        environment.lifecycle().manage(tickManager);
        ScheduledExecutorService queryScheduledExecutor = Executors.newScheduledThreadPool(
                2,
                new ThreadFactoryBuilder()
                        .setNameFormat("query-mgr-%d")
                        .build()
        );

        MetricQueryManager historyMetricQueryManager = new MetricQueryManager<HistoryMetricQueryEvent>(
                queryScheduledExecutor,
                config.metrics().getHistory().getInterval(),
                config.metrics().getHistory().getUnit(),
                provider,
                eventBus
        ) {
            @Override
            protected HistoryMetricQueryEvent event(SystemLoad load) {
                return new HistoryMetricQueryEvent(load);
            }
        };
        environment.lifecycle().manage(historyMetricQueryManager);
        MetricQueryManager monitorMetricQueryManager = new MetricQueryManager<MonitorMetricQueryEvent>(
                queryScheduledExecutor,
                config.metrics().getMonitor().getInterval(),
                config.metrics().getMonitor().getUnit(),
                provider,
                eventBus
        ) {
            @Override
            protected MonitorMetricQueryEvent event(SystemLoad load) {
                return new MonitorMetricQueryEvent(load);
            }
        };
        environment.lifecycle().manage(monitorMetricQueryManager);

        MetricsHistoryManager historyManager = new MetricsHistoryManager(config.metrics().getHistory(), eventBus);
        environment.lifecycle().manage(historyManager);

        JsonFile<HashMap<String, Monitor>> persistentMonitors =
                new JsonFile<HashMap<String, Monitor>>(
                        "monitors.json",
                        JsonFile.<com.krillsson.sysapi.dto.monitor.Monitor>mapTypeReference(),
                        new HashMap<>(),
                        environment.getObjectMapper()
                );

        MonitorManager monitorManager = new MonitorManager(eventBus, persistentMonitors, provider);
        environment.lifecycle().manage(monitorManager);

        environment.jersey().register(new SystemResource(os,
                                                         SystemInfo.getCurrentPlatformEnum(),
                                                         provider.cpuMetrics(),
                                                         provider.networkMetrics(),
                                                         provider.driveMetrics(),
                                                         provider.memoryMetrics(),
                                                         provider.processesMetrics(), provider.gpuMetrics(),
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
        environment.jersey().register(new EventResource(monitorManager));
        environment.jersey().register(new MonitorResource(monitorManager));
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
