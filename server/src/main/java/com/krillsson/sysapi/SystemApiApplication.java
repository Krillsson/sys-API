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
import com.krillsson.sysapi.core.MetricsProvider;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMixin;
import com.krillsson.sysapi.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.sslreload.SslReloadBundle;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.URL;
import java.time.Clock;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


public class SystemApiApplication extends Application<SystemApiConfiguration> {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SystemApiApplication.class.getSimpleName());
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
                .addFilter("networkInterface filter", SimpleBeanPropertyFilter.serializeAllExcept("name", "displayName", "inetAddresses", "interfaceAddresses", "mtu", "subInterfaces"));
        mapper.setFilters(filterProvider);
        bootstrap.addBundle(new SslReloadBundle());
    }

    @Override
    public void run(SystemApiConfiguration config, Environment environment) throws Exception {
        this.environment = environment;

        if (config.forwardHttps()) {
            addHttpsForward(environment.getApplicationContext());
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

        SpeedMeasurementManager speedMeasurementManager = new SpeedMeasurementManager(Executors.newScheduledThreadPool(2, new ThreadFactoryBuilder().setNameFormat("speed-mgr-%d").build()), Clock.systemUTC(), 5);
        InfoProvider provider = new MetricsProvider(hal, os, SystemInfo.getCurrentPlatformEnum(), config, speedMeasurementManager).create();
        environment.lifecycle().manage(speedMeasurementManager);

        environment.jersey().register(new SystemResource(provider));
        environment.jersey().register(new DiskStoresResource(provider));
        environment.jersey().register(new GpuResource(provider));
        environment.jersey().register(new MemoryResource(provider));
        environment.jersey().register(new NetworkInterfacesResource(provider));
        environment.jersey().register(new PowerSourcesResource(provider));
        environment.jersey().register(new ProcessesResource(provider));
        environment.jersey().register(new CpuResource(provider));
        environment.jersey().register(new SensorsResource(provider));
        environment.jersey().register(new MotherboardResource(provider));
        environment.jersey().register(new MetaInfoResource(getVersionFromManifest(), getEndpoints(environment), os.getProcessId()));
    }


    private void addHttpsForward(ServletContextHandler handler) {
        handler.addFilter(new FilterHolder(new Filter() {

            public void init(FilterConfig filterConfig) throws ServletException {
            }

            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                StringBuffer uri = ((HttpServletRequest) request).getRequestURL();
                if (uri.toString().startsWith("http://")) {
                    String location = "https://" + uri.substring("http://".length());
                    ((HttpServletResponse) response).sendRedirect(location);
                } else {
                    chain.doFilter(request, response);
                }
            }

            public void destroy() {
            }
        }), "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    private String getVersionFromManifest() throws IOException {
        Class clazz = SystemApiApplication.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            LOGGER.error("Unable to determine version");
            return "";
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                "/META-INF/MANIFEST.MF";
        Manifest manifest = new Manifest(new URL(manifestPath).openStream());
        Attributes attr = manifest.getMainAttributes();
        return "v." + attr.getValue("Version");
    }

    public Environment getEnvironment() {
        return environment;
    }

    private String[] getEndpoints(Environment environment) {
        final String NEWLINE = String.format("%n", new Object[0]);

        String[] arr = environment.jersey().getResourceConfig().getEndpointsInfo()
                .replace("The following paths were found for the configured resources:", "")
                .replace("    ", "")
                .replaceAll(" \\(.+?\\)", "")
                .split(NEWLINE);

        return Arrays.copyOfRange(arr, 2, arr.length - 1);
    }

}
