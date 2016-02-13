package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.provider.InfoProvider;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("gpus")
@Produces(MediaType.APPLICATION_JSON)
public class GpuResource extends Resource {
    private InfoProvider provider;

    public GpuResource(InfoProvider provider) {
        this.provider = provider;
    }

    @Override
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<Gpu> getRoot(@Auth UserConfiguration user) {
        return provider.gpus();
    }

}
