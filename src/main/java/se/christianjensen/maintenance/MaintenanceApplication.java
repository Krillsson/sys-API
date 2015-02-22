package se.christianjensen.maintenance;

import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import se.christianjensen.maintenance.auth.SimpleAuthenticator;
import se.christianjensen.maintenance.db.UserDAO;
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
    public void run(MaintenanceConfiguration config, Environment environment) throws Exception {
        UserDAO userDAO = new UserDAO();
        SigarMetrics sigarMetrics = SigarMetrics.getInstance();

        Client httpClient = new JerseyClientBuilder(environment).using(config.getJerseyClientConfiguration())
                .build(getName());


        environment.jersey().register(new BasicAuthProvider<User>(new SimpleAuthenticator(userDAO), "Maintenance-API"));
        environment.jersey().register(new CpuResource(sigarMetrics.cpu()));
        environment.jersey().register(new FilesystemResource(sigarMetrics.filesystems()));
        environment.jersey().register(new MemoryResource(sigarMetrics.memory()));
        environment.jersey().register(new SystemResource(sigarMetrics.system()));
        environment.jersey().register(new NetworkResource(sigarMetrics.network()));
    }
}
