package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.MonitorMapper
import com.krillsson.sysapi.dto.monitor.MonitorEvent
import io.dropwizard.auth.Auth
import java.util.*
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
class EventResource(private val eventManager: EventManager) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getRoot(@Auth user: UserConfiguration?): List<MonitorEvent> {
        val events = eventManager.getAll()
        return MonitorMapper.INSTANCE.map(events)
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun delete(@Auth user: UserConfiguration?, @PathParam("id") id: String?) {
        val removed = eventManager.remove(UUID.fromString(id))
        if (!removed) {
            throw WebApplicationException(Response.Status.NOT_FOUND)
        }
    }

}