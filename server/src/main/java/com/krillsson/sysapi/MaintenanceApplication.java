package com.krillsson.sysapi;


import com.krillsson.sysapi.auth.BasicAuthenticator;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.health.SigarLoadingHealthCheck;
import com.krillsson.sysapi.provider.InfoProvider;
import com.krillsson.sysapi.provider.InfoProviderFactory;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.sigar.SigarKeeper;


import com.krillsson.sysapi.util.OperatingSystem;
import io.dropwizard.Configuration;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.EnumSet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class MaintenanceApplication extends Application<MaintenanceConfiguration> {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MaintenanceApplication.class.getSimpleName());
    private Configuration config;

    public static void main(String[] args) throws Exception {
        new MaintenanceApplication().run(args);
    }

    @Override
    public String getName() {
        return "System-Api";
    }

    @Override
    public void initialize(Bootstrap<MaintenanceConfiguration> maintenanceConfigurationBootstrap) {

    }

    @Override
    public void run(MaintenanceConfiguration config, Environment environment) throws Exception {
        this.config = config;
        System.setProperty("org.hyperic.sigar.path", libLocation(config));
        SigarKeeper sigarKeeper = SigarKeeper.getInstance();
        InfoProviderFactory infoProviderFactory = InfoProviderFactory.initialize(OperatingSystem.getCurrentOperatingSystem());
        InfoProvider provider = infoProviderFactory.getInfoProvider();

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

        environment.jersey().register(new AuthDynamicFeature(userBasicCredentialAuthFilter));
        environment.jersey().register(new AuthValueFactoryProvider.Binder(UserConfiguration.class));
        environment.jersey().register(new CpuResource(provider));
        environment.jersey().register(new DriveResource(provider));
        environment.jersey().register(new MemoryResource(provider));
        environment.jersey().register(new SystemResource(provider));
        environment.jersey().register(new NetworkResource(provider));
        environment.jersey().register(new ProcessResource(provider));
        environment.jersey().register(new UsersResource(provider));
        environment.jersey().register(new GpuResource(provider));
        environment.jersey().register(new MotherboardResource(provider));
        environment.jersey().register(new MetaInfoResource(getVersionFromManifest(), provider));

        environment.healthChecks().register("Sigar", new SigarLoadingHealthCheck());
    }

    private String libLocation(MaintenanceConfiguration config) {
        if (config.getSigarLocation() != null) {
            return config.getSigarLocation();
        }

        File thisJar = new File(MaintenanceApplication.class.getProtectionDomain().getCodeSource().getLocation().getFile());

        String separator = System.getProperty("file.separator");
        String pathToJar = thisJar.getParent();
        try {
            pathToJar = URLDecoder.decode(pathToJar, "UTF-8");
            return pathToJar + separator + "lib";
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Unable to decode the path to UTF-8");
            return "";
        }
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
        Class clazz = MaintenanceApplication.class;
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

    public Configuration getConfig() {
        return config;
    }
}
