package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessesResource {
    OperatingSystem operatingSystem;

    public ProcessesResource(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public OSProcess[] getRoot(@Auth UserConfiguration user) {
        return operatingSystem.getProcesses(0, OperatingSystem.ProcessSort.MEMORY);
    }
}
