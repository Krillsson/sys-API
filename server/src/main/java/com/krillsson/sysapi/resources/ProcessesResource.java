package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.json.software.os.OSProcess;
import oshi.json.software.os.OperatingSystem;


import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessesResource {
    private final OperatingSystem operatingSystem;

    public ProcessesResource(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public OSProcess[] getRoot(@Auth UserConfiguration user) {
        return operatingSystem.getProcesses(0, oshi.software.os.OperatingSystem.ProcessSort.MEMORY);
    }
}
