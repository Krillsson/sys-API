package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("meta")
@Produces(MediaType.APPLICATION_JSON)
public class MetaInfoResource {

    private String version;

    public MetaInfoResource(String version) {
        this.version = version;
    }

    @Path("version")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public String getVersion(@Auth UserConfiguration user) {
        return version;
    }

}
