package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.monitoring.MonitorManager;
import com.krillsson.sysapi.core.monitoring.MonitorMapper;
import com.krillsson.sysapi.dto.monitor.Monitor;
import com.krillsson.sysapi.dto.monitor.MonitorCreated;
import com.krillsson.sysapi.dto.monitor.MonitorEvent;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("monitors")
@Produces(MediaType.APPLICATION_JSON)
public class MonitorResource {
    private final MonitorManager monitorManager;

    public MonitorResource(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<Monitor> getRoot(@Auth UserConfiguration user) {
        return MonitorMapper.INSTANCE.mapList(monitorManager.monitors());
    }

    @GET
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Monitor monitorById(@Auth UserConfiguration user, @PathParam("id") String id) {
        return MonitorMapper.INSTANCE.map(monitorManager.monitorById(id)
                                                  .orElseThrow(() -> new WebApplicationException(NOT_FOUND)));
    }

    @GET
    @Path("{id}/events")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<MonitorEvent> getEventForMonitorId(@Auth UserConfiguration user, @PathParam("id") String monitorId) {
        return MonitorMapper.INSTANCE.map(monitorManager.eventsForMonitorWithId(monitorId)
                                                  .orElseThrow(() -> new WebApplicationException(NOT_FOUND)));
    }

    @POST
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public MonitorCreated createMonitor(@Auth UserConfiguration user, Monitor monitor) {

        com.krillsson.sysapi.core.monitoring.Monitor monitorToBeCreated = Optional.ofNullable(MonitorMapper.INSTANCE.map(
                monitor)).orElseThrow(() -> new WebApplicationException(BAD_REQUEST));
        if (!monitorManager.validate(monitorToBeCreated)) {
            throw new WebApplicationException(String.format(
                    "Not mappable to device %s : %s",
                    monitor.getType().name(),
                    monitor.getId()
            ), Response.Status.NOT_FOUND);
        }
        String createdId = monitorManager.addMonitor(monitorToBeCreated);

        return new MonitorCreated(createdId);
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public void delete(@Auth UserConfiguration user, @PathParam("id") String id) {

        boolean removed = monitorManager.remove(id) != null;
        if (!removed) {
            throw new WebApplicationException(NOT_FOUND);
        }
    }
}
