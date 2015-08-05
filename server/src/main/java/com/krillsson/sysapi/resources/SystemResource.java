package com.krillsson.sysapi.resources;


import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.domain.system.Machine;
import com.krillsson.sysapi.sigar.SystemSigar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource extends Resource {
    private SystemSigar systemSigar;

    public SystemResource(SystemSigar systemSigar) {
        this.systemSigar = systemSigar;
    }

    @GET
    @Override
    public Machine getRoot(@Auth UserConfiguration user) {
        return systemSigar.machineInfo();
    }
}
