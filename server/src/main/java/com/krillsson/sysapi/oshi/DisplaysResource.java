package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.util.OperatingSystem;
import io.dropwizard.auth.Auth;
import ohmwrapper.GpuMonitor;
import oshi.hardware.Display;
import oshi.hardware.Sensors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("displays")
@Produces(MediaType.APPLICATION_JSON)
public class DisplaysResource {
    private final Display[] displays;

    public DisplaysResource(Display[] displays) {
        this.displays = displays;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Object getRoot(@Auth UserConfiguration user) {
        return displays;
    }
}
