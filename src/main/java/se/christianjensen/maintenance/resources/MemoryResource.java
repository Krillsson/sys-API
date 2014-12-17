package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.representation.memory.MainMemory;
import se.christianjensen.maintenance.representation.memory.MemoryInfo;
import se.christianjensen.maintenance.representation.memory.SwapSpace;
import se.christianjensen.maintenance.sigar.MemoryMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("memory")
@Produces(MediaType.APPLICATION_JSON)
public class MemoryResource {
    private MemoryMetrics memoryMetrics;

    public MemoryResource(MemoryMetrics memoryMetrics) {
        this.memoryMetrics = memoryMetrics;
    }

    @GET
    public MemoryInfo getMemoryInfo() {
        return memoryMetrics.getMemoryInfo();
    }

    @Path("ram")
    @GET
    public MainMemory getRam() {
        return memoryMetrics.getRam();
    }

    @Path("swap")
    @GET
    public SwapSpace getSwap() {
        return memoryMetrics.getSwap();
    }
}
