package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.monitoring.MonitorManager;
import com.krillsson.sysapi.core.monitoring.MonitorMapper;
import com.krillsson.sysapi.dto.monitor.MonitorEvent;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
    private final MonitorManager monitorManager;

    public EventResource(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<MonitorEvent> getRoot(@Auth UserConfiguration user) {
        return MonitorMapper.INSTANCE.map(monitorManager.events());
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public void delete(@Auth UserConfiguration user, @PathParam("id") String id) {
        boolean removed = monitorManager.removeEvents(id);
        if (!removed) {
            throw new WebApplicationException(NOT_FOUND);
        }
    }
}
