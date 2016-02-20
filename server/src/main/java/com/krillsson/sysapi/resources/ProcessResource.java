package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;
import com.krillsson.sysapi.provider.InfoProvider;
import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.domain.processes.Process;
import com.krillsson.sysapi.sigar.ProcessSigar;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("processes")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource extends Resource {

    InfoProvider provider;

    public ProcessResource(InfoProvider provider) {
        this.provider = provider;
    }

    @Override
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<Process> getRoot(@Auth UserConfiguration user) {
        return provider.processes();
    }

    @Path("{pid}")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Process getProcessByPid(@Auth UserConfiguration user, @PathParam("pid") long pid) {
        try {
            return provider.getProcessByPid(pid);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }

    @Path("statistics")
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public ProcessStatistics getProcessStatistics(@Auth UserConfiguration user){
        return provider.statistics();
    }
}
