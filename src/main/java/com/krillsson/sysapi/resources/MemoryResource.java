package com.krillsson.sysapi.resources;

import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.representation.config.UserConfiguration;
import com.krillsson.sysapi.representation.memory.MainMemory;
import com.krillsson.sysapi.representation.memory.MemoryInfo;
import com.krillsson.sysapi.representation.memory.SwapSpace;
import com.krillsson.sysapi.sigar.MemoryMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("memory")
@Produces(MediaType.APPLICATION_JSON)
public class MemoryResource extends Resource {
    private MemoryMetrics memoryMetrics;

    public MemoryResource(MemoryMetrics memoryMetrics) {
        this.memoryMetrics = memoryMetrics;
    }

    @Override
    @GET
    public MemoryInfo getRoot(@Auth UserConfiguration user) {
        return memoryMetrics.getMemoryInfo();
    }

    @Path("ram")
    @GET
    public MainMemory getRam(@Auth UserConfiguration user) {
        return memoryMetrics.getRam();
    }

    @Path("swap")
    @GET
    public SwapSpace getSwap(@Auth UserConfiguration user) {
        return memoryMetrics.getSwap();
    }
}
