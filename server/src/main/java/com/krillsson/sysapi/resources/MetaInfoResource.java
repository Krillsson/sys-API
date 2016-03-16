package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.provider.InfoProvider;
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
    private InfoProvider provider;

    public MetaInfoResource(String version, InfoProvider provider) {
        this.version = version;
        this.provider = provider;
    }

    @Path("version")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public String getVersion(@Auth UserConfiguration user){
        return version;
    }

    @Path("pid")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public long getThisPid(@Auth UserConfiguration user){
        return provider.getThisProcessPid();
    }

}
