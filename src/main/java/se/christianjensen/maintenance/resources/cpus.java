package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.capturing.Cpu.Cpu;
import se.christianjensen.maintenance.capturing.InformationProviderInterface;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("cpus")
@Produces(MediaType.APPLICATION_JSON)
public class cpus {
    private InformationProviderInterface provider;

    public cpus(InformationProviderInterface provider) {
        this.provider = provider;
    }

    @GET
    public List<Cpu> getCpus() {
        return provider.getCpus();
    }
}
