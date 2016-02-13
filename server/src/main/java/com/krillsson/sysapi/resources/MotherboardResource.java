package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.motherboard.Motherboard;
import com.krillsson.sysapi.provider.InfoProvider;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("motherboard")
@Produces(MediaType.APPLICATION_JSON)
public class MotherboardResource extends Resource {
    private InfoProvider provider;

    public MotherboardResource(InfoProvider provider) {
        this.provider = provider;
    }

    @Override
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Motherboard getRoot(@Auth UserConfiguration user) {
        return provider.motherboard();
    }

}
