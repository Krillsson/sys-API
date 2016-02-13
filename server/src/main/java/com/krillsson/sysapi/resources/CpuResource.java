package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.provider.InfoProvider;
import io.dropwizard.auth.Auth;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuInfo;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.sigar.CpuSigar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource extends Resource {

    private InfoProvider provider;

    public CpuResource(InfoProvider provider) {
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @Override
    public Cpu getRoot(@Auth UserConfiguration user) {
        return provider.cpu();
    }

    @Path("{core}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @GET
    public CpuLoad getCpuTimeByCore(@Auth UserConfiguration user, @PathParam("core") int core) {
        try {
            return provider.getCpuTimeByCoreIndex(core);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }
}
