package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.hardware.GlobalMemory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("memory")
@Produces(MediaType.APPLICATION_JSON)
public class MemoryResource {
    private GlobalMemory memory;

    public MemoryResource(GlobalMemory memory) {
        this.memory = memory;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public GlobalMemory getRoot(@Auth UserConfiguration user) {
        return memory ;
    }
}
