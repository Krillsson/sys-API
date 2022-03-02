package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.monitor.toFractionalValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.graphql.mutations.CreateConditionalMonitorInput
import com.krillsson.sysapi.graphql.mutations.CreateFractionMonitorInput
import com.krillsson.sysapi.graphql.mutations.CreateMonitorOutput
import com.krillsson.sysapi.graphql.mutations.CreateNumericalMonitorInput
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
    fun getRoot(): List<Monitor<MonitoredValue>> {
        return monitorManager.getAll()
    }

    @GET
    @Path("{id}")
    fun monitorById(@PathParam("id") id: String): Monitor<MonitoredValue> {
       return monitorManager.getById(UUID.fromString(id))
                ?: throw WebApplicationException("No monitor with id $id was found", Response.Status.NOT_FOUND)
    }

    @GET
    @Path("{id}/events")
    fun getEventForMonitorId(@PathParam("id") monitorId: String?): List<Event> {
        return eventManager.eventsForMonitorId(UUID.fromString(monitorId))
    }

    @POST
    @Path("numerical")
    fun createNumericalMonitor(input: CreateNumericalMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toNumericalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    @POST
    @Path("fractional")
    fun createFractionalMonitor(input: CreateFractionMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toFractionalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    @POST
    @Path("boolean")
    fun createBooleanMonitor(input: CreateConditionalMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toConditionalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
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