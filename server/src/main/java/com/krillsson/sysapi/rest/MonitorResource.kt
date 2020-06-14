package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.MonitorMapper
import com.krillsson.sysapi.dto.monitor.CreateMonitor
import com.krillsson.sysapi.dto.monitor.Monitor
import com.krillsson.sysapi.dto.monitor.MonitorCreated
import com.krillsson.sysapi.dto.monitor.MonitorEvent
import io.dropwizard.auth.Auth
import java.time.Duration
import java.util.*
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("monitors")
@Produces(MediaType.APPLICATION_JSON)
class MonitorResource(private val monitorManager: MonitorManager, private val eventManager: EventManager) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getRoot(@Auth user: UserConfiguration?): List<Monitor> {
        return MonitorMapper.INSTANCE.mapList(monitorManager.getAll())
    }

    @GET
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun monitorById(@Auth user: UserConfiguration?, @PathParam("id") id: String): Monitor {
        val monitor = monitorManager.getById(UUID.fromString(id))
                ?: throw WebApplicationException("No monitor with id $id was found", Response.Status.NOT_FOUND)
        return MonitorMapper.INSTANCE.map(monitor)
    }

    @GET
    @Path("{id}/events")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getEventForMonitorId(@Auth user: UserConfiguration?, @PathParam("id") monitorId: String?): List<MonitorEvent> {
        val events = eventManager.eventsForMonitorId(UUID.fromString(monitorId))
        return MonitorMapper.INSTANCE.map(events)
    }

    @POST
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun createMonitor(@Auth user: UserConfiguration?, monitor: CreateMonitor): MonitorCreated {
        return try {
            val added = monitorManager.add(Duration.ofSeconds(monitor.inertiaInSeconds), MonitorMapper.INSTANCE.map(monitor.type), monitor.threshold, monitor.idToMonitor)
            MonitorCreated(added.toString())
        } catch (e: IllegalArgumentException) {
            throw WebApplicationException(e.message, Response.Status.NOT_FOUND)
        }
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun delete(@Auth user: UserConfiguration?, @PathParam("id") id: String?) {
        val removed = monitorManager.remove(UUID.fromString(id))
        if (!removed) {
            throw WebApplicationException(Response.Status.NOT_FOUND)
        }
    }

}