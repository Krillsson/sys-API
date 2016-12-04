package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.hardware.Sensors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorsResource {
    private final Sensors sensors;

    public SensorsResource(Sensors sensors) {
        this.sensors = sensors;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Sensors getRoot(@Auth UserConfiguration user) {
        return sensors;
    }
}
