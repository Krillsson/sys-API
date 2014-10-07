package se.christianjensen.maintenance.resources;


import se.christianjensen.maintenance.sigar.SystemSigar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {
    private SystemSigar systemSigar;

    public SystemResource(SystemSigar systemSigar) {
        this.systemSigar = systemSigar;
    }

    @GET
    @Path("os")
    public String model(){
        return systemSigar.osName();
    }
}
