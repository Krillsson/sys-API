package se.christianjensen.maintenance;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.christianjensen.maintenance.metrics.SigarWrapper;
import se.christianjensen.maintenance.resources.CpuResource;


public class MaintenanceApplication extends Application<MaintenanceConfiguration> {
    public static void main(String[] args) throws Exception {
        new MaintenanceApplication().run(args);
    }


    @Override
    public String getName() {
        return "maintenance-api";
    }

    @Override
    public void initialize(Bootstrap<MaintenanceConfiguration> maintenanceConfigurationBootstrap) {

    }

    @Override
    public void run(MaintenanceConfiguration maintenanceConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new CpuResource(new SigarWrapper()));
    }
}
