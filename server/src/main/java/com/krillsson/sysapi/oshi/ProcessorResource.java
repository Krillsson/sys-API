package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.hardware.CentralProcessor;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("processor")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessorResource {
    private CentralProcessor processor;

    public ProcessorResource(CentralProcessor processor) {
        this.processor = processor;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public CentralProcessor getRoot(@Auth UserConfiguration user) {
        return processor;
    }
}
