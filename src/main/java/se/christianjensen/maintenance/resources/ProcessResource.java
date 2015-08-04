package se.christianjensen.maintenance.resources;

import io.dropwizard.auth.Auth;
import se.christianjensen.maintenance.representation.config.UserConfiguration;
import se.christianjensen.maintenance.representation.processes.Process;
import se.christianjensen.maintenance.sigar.ProcessMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("process")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessResource extends Resource {

    ProcessMetrics processMetrics;

    public ProcessResource(ProcessMetrics processMetrics) {
        this.processMetrics = processMetrics;
    }

    @Override
    @GET
    public List<Process> getRoot(@Auth UserConfiguration user) {
        return processMetrics.getProcesses();
    }

    @Path("{pid}")
    @GET
    public Process getProcessByPid(@Auth UserConfiguration user, @PathParam("pid") long pid) {
        try {
            return processMetrics.getProcessByPid(pid);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
    }
}
