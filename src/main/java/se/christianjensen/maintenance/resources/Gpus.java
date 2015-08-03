package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.capturing.Gpu.Gpu;
import se.christianjensen.maintenance.capturing.InformationProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("gpus")
@Produces(MediaType.APPLICATION_JSON)
public class Gpus {
    private InformationProvider provider;

    public Gpus(InformationProvider provider) {
        this.provider = provider;
    }

    @GET
    public List<Gpu> getGpus() {
        return provider.getGpus();
    }
}
