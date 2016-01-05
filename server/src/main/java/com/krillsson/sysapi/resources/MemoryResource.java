package com.krillsson.sysapi.resources;

import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.memory.MemoryInfo;
import com.krillsson.sysapi.domain.memory.SwapSpace;
import com.krillsson.sysapi.sigar.MemorySigar;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("memory")
@Produces(MediaType.APPLICATION_JSON)
public class MemoryResource extends Resource {
    private MemorySigar memorySigar;

    public MemoryResource(MemorySigar memorySigar) {
        this.memorySigar = memorySigar;
    }

    @Override
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public MemoryInfo getRoot(@Auth UserConfiguration user) {
        return memorySigar.getMemoryInfo();
    }

    @Path("ram")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public MainMemory getRam(@Auth UserConfiguration user) {
        return memorySigar.getRam();
    }

    @Path("swap")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public SwapSpace getSwap(@Auth UserConfiguration user) {
        return memorySigar.getSwap();
    }
}
