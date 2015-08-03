package se.christianjensen.maintenance.resources;

import io.dropwizard.auth.Auth;
import se.christianjensen.maintenance.representation.config.UserConfiguration;
import se.christianjensen.maintenance.representation.cpu.Cpu;
import se.christianjensen.maintenance.representation.cpu.CpuTime;
import se.christianjensen.maintenance.sigar.CpuMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource extends Resource {

    private CpuMetrics cpuMetrics;

    public CpuResource(CpuMetrics cpuMetrics) {
    this.cpuMetrics = cpuMetrics;
    }

    @GET
    @Override
    public Cpu getRoot(@Auth UserConfiguration user) {
        return cpuMetrics.getCpu();
    }

    @Path("{id}")
    @GET
    public CpuTime getCpuTimeByCore(@Auth UserConfiguration user, @PathParam("id") int id) {
        try {
            return cpuMetrics.getCpuTimeByCoreIndex(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }
}
