package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.monitoring.event.EventManager
import java.util.*
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
class EventResource(private val eventManager: EventManager) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getRoot(): List<Event> {
        return eventManager.getAll()
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun delete(@PathParam("id") id: String?) {
        val removed = eventManager.remove(UUID.fromString(id))
        if (!removed) {
            throw WebApplicationException(Response.Status.NOT_FOUND)
        }
    }
}