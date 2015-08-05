package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.domain.processes.ProcessStatistics;
import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.domain.processes.Process;
import com.krillsson.sysapi.sigar.ProcessSigar;

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

    ProcessSigar processSigar;

    public ProcessResource(ProcessSigar processSigar) {
        this.processSigar = processSigar;
    }

    @Override
    @GET
    public List<Process> getRoot(@Auth UserConfiguration user) {
        return processSigar.getProcesses();
    }

    @Path("{pid}")
    @GET
    public Process getProcessByPid(@Auth UserConfiguration user, @PathParam("pid") long pid) {
        try {
            return processSigar.getProcessByPid(pid);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }

    @Path("statistics")
    @GET
    public ProcessStatistics getProcessStatistics(@Auth UserConfiguration user){
        return processSigar.getStatistics();
    }
}
