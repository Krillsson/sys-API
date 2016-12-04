package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.hardware.PowerSource;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("powersources")
@Produces(MediaType.APPLICATION_JSON)
public class PowerSourcesResource {
    private final PowerSource[] powerSources;

    public PowerSourcesResource(PowerSource[] powerSources) {
        this.powerSources = powerSources;
    }
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public PowerSource[] getRoot(@Auth UserConfiguration user) {
        return powerSources;
    }
}
