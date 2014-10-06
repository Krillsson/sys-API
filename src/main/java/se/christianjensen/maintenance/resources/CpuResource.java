package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.metrics.SigarWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("info")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private SigarWrapper sigarWrapper;

    public CpuResource(SigarWrapper sigarWrapper) {
    this.sigarWrapper = sigarWrapper;
    }

    @GET
    @Path("model")
    public String Model(){
        return sigarWrapper.CpuModel();
    }

    @GET
    public ArrayList<String> All(){
        return new ArrayList<>();
    }
}
