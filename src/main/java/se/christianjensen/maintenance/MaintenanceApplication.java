package se.christianjensen.maintenance;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.christianjensen.maintenance.auth.SimpleAuthenticator;
import se.christianjensen.maintenance.auth.UserDAO;
import se.christianjensen.maintenance.preferences.JsonPreferences;
import se.christianjensen.maintenance.representation.internal.User;
import se.christianjensen.maintenance.resources.*;
import se.christianjensen.maintenance.sigar.SigarMetrics;


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
        UserDAO userDAO = new UserDAO();
        userDAO.createUser("Christian", "password");

        SigarMetrics sigarMetrics = SigarMetrics.getInstance();
        environment.jersey().register(new BasicAuthProvider<User>(new SimpleAuthenticator(userDAO), "Maintenance-API"));
        environment.jersey().register(new CpuResource(sigarMetrics.cpu()));
        environment.jersey().register(new FilesystemResource(sigarMetrics.filesystems()));
        environment.jersey().register(new MemoryResource(sigarMetrics.memory()));
        environment.jersey().register(new SystemResource(sigarMetrics.system()));
        environment.jersey().register(new NetworkResource(sigarMetrics.network()));
    }
}
