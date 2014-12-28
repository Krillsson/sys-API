package se.christianjensen.maintenance.resources;


import io.dropwizard.auth.Auth;
import se.christianjensen.maintenance.representation.internal.User;
import se.christianjensen.maintenance.representation.system.Machine;
import se.christianjensen.maintenance.sigar.SystemMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {
    private SystemMetrics systemMetrics;

    public SystemResource(SystemMetrics systemMetrics) {
        this.systemMetrics = systemMetrics;
    }

    @GET
    public Machine all(@Auth User user){
        return systemMetrics.machineInfo();
    }

}
