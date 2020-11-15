package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.core.domain.event.MonitorEvent
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorManager
import java.time.Duration
import java.util.*
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("monitors")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
class MonitorResource(private val monitorManager: MonitorManager, private val eventManager: EventManager) {
    @GET
    fun getRoot(): List<Monitor> {
        return monitorManager.getAll()
    }

    @GET
    @Path("{id}")
    fun monitorById(@PathParam("id") id: String): Monitor {
       return monitorManager.getById(UUID.fromString(id))
                ?: throw WebApplicationException("No monitor with id $id was found", Response.Status.NOT_FOUND)
    }

    @GET
    @Path("{id}/events")
    fun getEventForMonitorId(@PathParam("id") monitorId: String?): List<MonitorEvent> {
        return eventManager.eventsForMonitorId(UUID.fromString(monitorId))
    }

    @POST
    fun createMonitor(monitor: CreateMonitor): MonitorCreated {
        return try {
            val added = monitorManager.add(Duration.ofSeconds(monitor.inertiaInSeconds), monitor.type, monitor.threshold, monitor.idToMonitor)
            MonitorCreated(added.toString())
        } catch (e: IllegalArgumentException) {
            throw WebApplicationException(e.message, Response.Status.NOT_FOUND)
        }
    }

    @DELETE
    @Path("{id}")
    fun delete(@PathParam("id") id: String?) {
        val removed = monitorManager.remove(UUID.fromString(id))
        if (!removed) {
            throw WebApplicationException(Response.Status.NOT_FOUND)
        }
    }

}