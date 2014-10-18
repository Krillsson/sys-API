package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.sigar.CpuMetrics;
import se.christianjensen.maintenance.sigar.old.CpuSigar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private CpuMetrics cpuMetrics;

    public CpuResource(CpuMetrics cpuMetrics) {
    this.cpuMetrics = cpuMetrics;
    }

    @GET
    public CpuMetrics All(){
        return cpuMetrics;
    }
}
