package com.krillsson.sysapi;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.krillsson.sysapi.auth.SimpleAuthenticator;
import com.krillsson.sysapi.health.SigarLoadingHealthCheck;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.sigar.SigarKeeper;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class MaintenanceApplication extends Application<MaintenanceConfiguration> {
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MaintenanceApplication.class.getSimpleName());


    public static void main(String[] args) throws Exception {
        new MaintenanceApplication().run(args);
    }


    @Override
    public String getName() {
        return "sysapi-api";
    }

    @Override
    public void initialize(Bootstrap<MaintenanceConfiguration> maintenanceConfigurationBootstrap) {

    }

    @Override
    public void run(MaintenanceConfiguration config, Environment environment) throws Exception {
        System.setProperty("org.hyperic.sigar.path", libLocation());
        SigarKeeper sigarKeeper = SigarKeeper.getInstance();

        environment.jersey().register(new BasicAuthProvider<>(new SimpleAuthenticator(config.getUser()), "System-Api"));
        environment.jersey().register(new CpuResource(sigarKeeper.cpu()));
        environment.jersey().register(new FilesystemResource(sigarKeeper.filesystems()));
        environment.jersey().register(new MemoryResource(sigarKeeper.memory()));
        environment.jersey().register(new SystemResource(sigarKeeper.system()));
        environment.jersey().register(new NetworkResource(sigarKeeper.network()));
        environment.jersey().register(new ProcessResource(sigarKeeper.process()));

        environment.healthChecks().register("Sigar", new SigarLoadingHealthCheck());
    }

    private String libLocation()
    {
        String separator = System.getProperty("file.separator");
        String path = MaintenanceApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(0, path.lastIndexOf(separator));
        try {
            String jarlocation = URLDecoder.decode(path, "UTF-8");
            return jarlocation + separator +"lib";
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Unable to decode the path to UTF-8");
            return "";
        }
    }
}
