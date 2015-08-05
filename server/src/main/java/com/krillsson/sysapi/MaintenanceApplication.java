package com.krillsson.sysapi;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.krillsson.sysapi.auth.SimpleAuthenticator;
import com.krillsson.sysapi.health.SigarLoadingHealthCheck;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.sigar.SigarKeeper;
import org.slf4j.Logger;

import java.io.File;
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
        System.setProperty("org.hyperic.sigar.path", libLocation(config));
        SigarKeeper sigarKeeper = SigarKeeper.getInstance();

        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<UserConfiguration>(new SimpleAuthenticator(config.getUser()), "System-Api", UserConfiguration.class)));
        environment.jersey().register(new CpuResource(sigarKeeper.cpu()));
        environment.jersey().register(new FilesystemResource(sigarKeeper.filesystems()));
        environment.jersey().register(new MemoryResource(sigarKeeper.memory()));
        environment.jersey().register(new SystemResource(sigarKeeper.system()));
        environment.jersey().register(new NetworkResource(sigarKeeper.network()));
        environment.jersey().register(new ProcessResource(sigarKeeper.process()));

        environment.healthChecks().register("Sigar", new SigarLoadingHealthCheck());
    }

    private String libLocation(MaintenanceConfiguration config)
    {
        if(config.getSigarLocation() != null)
        {
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
}
