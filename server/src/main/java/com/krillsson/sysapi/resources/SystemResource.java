package com.krillsson.sysapi.resources;


import com.krillsson.sysapi.domain.system.*;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.sigar.SystemSigar;
import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;

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
    public System getRoot(@Auth UserConfiguration user) {
        return systemSigar.getSystem();
    }

    @Path("jvm")
    @GET
    public JvmProperties getJvmProperties(@Auth UserConfiguration user) {
        return systemSigar.getJvmProperties();
    }

    @Path("operatingsystem")
    @GET
    public OperatingSystem getOperatingSystem(@Auth UserConfiguration user) {
        return systemSigar.getOperatingSystem();
    }

    @Path("uptime")
    @GET
    public double getUptime(@Auth UserConfiguration user) {
        return systemSigar.getUptime();
    }
}
