package se.krillsson.maintenance.resources;

import io.dropwizard.auth.Auth;
import se.krillsson.maintenance.representation.config.UserConfiguration;
import se.krillsson.maintenance.representation.cpu.Cpu;
import se.krillsson.maintenance.representation.cpu.CpuTime;
import se.krillsson.maintenance.sigar.CpuMetrics;

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

    @Path("{core}")
    @GET
    public CpuTime getCpuTimeByCore(@Auth UserConfiguration user, @PathParam("core") int core) {
        try {
            return cpuMetrics.getCpuTimeByCoreIndex(core);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }
}
