package se.christianjensen.maintenance;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.christianjensen.maintenance.resources.SystemResource;
import se.christianjensen.maintenance.sigar.CpuSigar;
import se.christianjensen.maintenance.sigar.SigarWrapper;
import se.christianjensen.maintenance.resources.CpuResource;
import se.christianjensen.maintenance.sigar.SystemSigar;


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
        CpuSigar cpuSigar = new CpuSigar();
        SystemSigar systemSigar = new SystemSigar();
        environment.jersey().register(new CpuResource(cpuSigar));
        environment.jersey().register(new SystemResource(systemSigar));

    }
}
