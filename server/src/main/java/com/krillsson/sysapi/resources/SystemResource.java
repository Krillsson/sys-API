package com.krillsson.sysapi.resources;


import com.google.common.base.Optional;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.system.*;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.sigar.SystemSigar;
import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public System getRoot(@Auth UserConfiguration user) {
        return systemSigar.getSystem();
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
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
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public JvmProperties getJvmProperties(@Auth UserConfiguration user) {
        return systemSigar.getJvmProperties();
    }

    @Path("operatingsystem")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public OperatingSystem getOperatingSystem(@Auth UserConfiguration user) {
        return systemSigar.getOperatingSystem();
    }

    @Path("uptime")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public double getUptime(@Auth UserConfiguration user) {
        return systemSigar.getUptime();
    }
}
