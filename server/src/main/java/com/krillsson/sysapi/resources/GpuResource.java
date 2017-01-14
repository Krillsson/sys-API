package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.gpu.GpuInfo;
import com.krillsson.sysapi.core.InfoProvider;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.Display;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("gpus")
@Produces(MediaType.APPLICATION_JSON)
public class GpuResource {
    private final Display[] displays;
    private InfoProvider provider;

    public GpuResource(Display[] displays, InfoProvider provider) {
        this.displays = displays;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public GpuInfo getRoot(@Auth UserConfiguration user) {
        return new GpuInfo(displays, provider.gpus());
    }
}
