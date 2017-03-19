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
import com.krillsson.sysapi.auth.BasicAuthenticator;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.SystemApiConfiguration;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.InfoProviderFactory;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMixin;
import com.krillsson.sysapi.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Arrays;
import java.util.EnumSet;
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
    public void initialize(Bootstrap<SystemApiConfiguration> maintenanceConfigurationBootstrap) {
        ObjectMapper mapper = maintenanceConfigurationBootstrap.getObjectMapper();
        mapper.addMixInAnnotations(NetworkInterface.class, NetworkInterfaceMixin.class);
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("networkInterface filter", SimpleBeanPropertyFilter.serializeAllExcept("name", "displayName", "inetAddresses", "interfaceAddresses", "mtu", "subInterfaces"));
        mapper.setFilters(filterProvider);
        //mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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

        InfoProvider provider = new InfoProviderFactory(hal, SystemInfo.getCurrentPlatformEnum(), config).provide();
        Sensors sensors = hal.getSensors();

        environment.jersey().register(new SystemResource(provider, os, hal.getProcessor(), hal.getMemory(), hal.getPowerSources(), hal.getSensors()));
        environment.jersey().register(new DiskStoresResource(hal.getDiskStores(), os.getFileSystem(), provider));
        environment.jersey().register(new GpuResource(hal.getDisplays(), provider));
        environment.jersey().register(new MemoryResource(hal.getMemory()));
        environment.jersey().register(new NetworkInterfacesResource(provider));
        environment.jersey().register(new PowerSourcesResource(hal.getPowerSources()));
        environment.jersey().register(new ProcessesResource(os, hal.getMemory()));
        environment.jersey().register(new CpuResource(os, sensors, hal.getProcessor(), provider));
        environment.jersey().register(new SensorsResource(sensors, provider));
        environment.jersey().register(new MotherboardResource(hal.getComputerSystem(), hal.getUsbDevices(false)));
        environment.jersey().register(new MetaInfoResource(getVersionFromManifest(), getEndpoints(environment)));
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
        return attr.getValue("Version");
    }

    public Environment getEnvironment() {
        return environment;
    }

    private String[] getEndpoints(Environment environment) {
        final String NEWLINE = String.format("%n", new Object[0]);

        String[] arr = environment.jersey().getResourceConfig().getEndpointsInfo()
                .replace("The following paths were found for the configured resources:", "")
                .replace("    ", "")
                .replace("com.krillsson.sysapi.resources.", "")
                .split(NEWLINE);

        return Arrays.copyOfRange(arr, 2, arr.length - 1);
    }

}
