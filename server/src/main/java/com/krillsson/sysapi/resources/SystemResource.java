package com.krillsson.sysapi.resources;


import com.google.common.base.Optional;
import com.krillsson.sysapi.domain.system.*;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.sigar.SystemSigar;
import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource extends Resource {
    private SystemSigar systemSigar;

    public SystemResource(SystemSigar systemSigar) {
        this.systemSigar = systemSigar;
    }

    @Override
    public System getRoot(@Auth UserConfiguration user) {
        return systemSigar.getSystem();
    }

    @GET
    public System getExtendedSystem(@Auth UserConfiguration user, @QueryParam("fsid") Optional<String> filesystemId, @QueryParam("nicid") Optional<String> nicId)
    {
        if(filesystemId.isPresent() && nicId.isPresent())
        {
            try {
                return systemSigar.getExtendedSystem(filesystemId.get(), nicId.get());
            } catch (IllegalArgumentException e) {
                throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
            }
        }
        else
        {
            return systemSigar.getSystem();
        }
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
