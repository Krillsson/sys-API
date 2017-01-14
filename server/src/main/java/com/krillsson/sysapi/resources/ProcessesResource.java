package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.system.ProcessesInfo;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.GlobalMemory;
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
    private GlobalMemory memory;

    public ProcessesResource(OperatingSystem operatingSystem, GlobalMemory memory) {
        this.operatingSystem = operatingSystem;
        this.memory = memory;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public ProcessesInfo getRoot(@Auth UserConfiguration user) {
        return new ProcessesInfo(memory, operatingSystem.getProcesses(0, oshi.software.os.OperatingSystem.ProcessSort.MEMORY));
    }
}
