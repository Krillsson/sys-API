package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.metrics.SigarWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("info")
@Produces(MediaType.APPLICATION_JSON)
public class InfoResource {

    @GET
    @Path("osname")
    public String osName(){
        SigarWrapper sigarWrapper = new SigarWrapper();
        return sigarWrapper.osName();
    }

    @GET
    @Path("cpumodel")
    public String cpuModel(){
        SigarWrapper sigarWrapper = new SigarWrapper();
        return sigarWrapper.CpuModel();
    }
}
