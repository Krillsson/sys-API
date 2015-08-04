package com.krillsson.sysapi;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.krillsson.sysapi.auth.SimpleAuthenticator;
import com.krillsson.sysapi.health.SigarLoadingHealthCheck;
import com.krillsson.sysapi.resources.*;
import com.krillsson.sysapi.sigar.SigarKeeper;


public class MaintenanceApplication extends Application<MaintenanceConfiguration> {
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
        System.setProperty("org.hyperic.sigar.path", config.getSigarLocation());
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
}
