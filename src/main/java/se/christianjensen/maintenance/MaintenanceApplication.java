package se.christianjensen.maintenance;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.christianjensen.maintenance.resources.FilesystemResource;
import se.christianjensen.maintenance.resources.MemoryResource;
import se.christianjensen.maintenance.resources.SystemResource;
import se.christianjensen.maintenance.sigar.SigarMetrics;
import se.christianjensen.maintenance.sigar.old.CpuSigar;
import se.christianjensen.maintenance.resources.CpuResource;
import se.christianjensen.maintenance.sigar.old.SystemSigar;


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
        SigarMetrics sigarMetrics = SigarMetrics.getInstance();

        environment.jersey().register(new CpuResource(sigarMetrics.cpu()));
        environment.jersey().register(new FilesystemResource(sigarMetrics.filesystems()));
        environment.jersey().register(new MemoryResource(sigarMetrics.memory()));

    }
}
