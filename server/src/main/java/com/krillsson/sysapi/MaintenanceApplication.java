package com.krillsson.sysapi;


import com.krillsson.sysapi.auth.BasicAuthenticator;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.health.SigarLoadingHealthCheck;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.sigar.SigarKeeper;


import net.sf.jni4net.Bridge;
import ohmwrapper.DriveMonitor;
import ohmwrapper.MonitorManager;
import ohmwrapper.OHMManagerFactory;
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
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class MaintenanceApplication extends Application<MaintenanceConfiguration> {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MaintenanceApplication.class.getSimpleName());

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
        System.setProperty("org.hyperic.sigar.path", libLocation(config));
        SigarKeeper sigarKeeper = SigarKeeper.getInstance();

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
        environment.jersey().register(new CpuResource(sigarKeeper.cpu()));
        environment.jersey().register(new FilesystemResource(sigarKeeper.filesystems()));
        environment.jersey().register(new MemoryResource(sigarKeeper.memory()));
        environment.jersey().register(new SystemResource(sigarKeeper.system()));
        environment.jersey().register(new NetworkResource(sigarKeeper.network()));
        environment.jersey().register(new ProcessResource(sigarKeeper.process()));
        environment.jersey().register(new UsersResource(sigarKeeper.system()));

        environment.healthChecks().register("Sigar", new SigarLoadingHealthCheck());
        ohmjniwrapper();
    }

    private void ohmjniwrapper() throws IOException {
        /*Bridge.setVerbose(true);
        Bridge.init();
        //For testing
        //File file = new File("server/lib/OhmJniWrapper.dll");
        //File anotherFile = new File("server/lib/OhmJniWrapper.j4n.dll");
        //File anotherFileAgain = new File("server/lib/OpenHardwareMonitorLib.dll");

        //For deploying
        File file = new File("lib/OhmJniWrapper.dll");
        File anotherFile = new File("lib/OhmJniWrapper.j4n.dll");
        File anotherFileAgain = new File("lib/OpenHardwareMonitorLib.dll");

        Bridge.LoadAndRegisterAssemblyFrom(file);
        Bridge.LoadAndRegisterAssemblyFrom(anotherFile);
        Bridge.LoadAndRegisterAssemblyFrom(anotherFileAgain);
        OHMManagerFactory factory = new OHMManagerFactory();
        factory.init();
        MonitorManager monitorManager = factory.GetManager();
        monitorManager.Update();
        List<DriveMonitor> drivemonitors = Arrays.asList(monitorManager.DriveMonitors());
        for (DriveMonitor drive :
                drivemonitors) {
            LOGGER.info("Drive temp: {}", drive.getTemperature().getValue());
            LOGGER.info("Drive name: {}", drive.getName());
            LOGGER.info("Drive logicalname: {}", drive.getLogicalName());
        }*/
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

    void addHttpsForward(ServletContextHandler handler) {
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


}
