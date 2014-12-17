package se.christianjensen.maintenance.resources;

import com.sun.jersey.api.NotFoundException;
import se.christianjensen.maintenance.representation.cpu.Cpu;
import se.christianjensen.maintenance.representation.cpu.CpuTime;
import se.christianjensen.maintenance.sigar.CpuMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private CpuMetrics cpuMetrics;

    public CpuResource(CpuMetrics cpuMetrics) {
    this.cpuMetrics = cpuMetrics;
    }

    @GET
    public Cpu getCpuInfo() {
        return cpuMetrics.getCpu();
    }

    @Path("{id}")
    @GET
    public CpuTime getCpuTimeByCore(@PathParam("id") int id) {
        try {
            return cpuMetrics.getCpuTimeByCoreIndex(id);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
