/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.rest

import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import java.time.OffsetDateTime
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("nics")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
class NetworkInterfacesResource(
    private val infoProvider: NetworkMetrics,
    private val historyManager: MetricsHistoryManager
) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun networkInterfaces(): List<NetworkInterface> {
        return infoProvider.networkInterfaces()
    }

    @GET
    @Path("loads")
    fun networkInterfaceLoads(): List<NetworkInterfaceLoad> {
        return infoProvider.networkInterfaceLoads()
    }

    @GET
    @Path("{id}")
    fun networkInterfaceById(
        @PathParam("id") id: String
    ): NetworkInterface {
        return infoProvider.networkInterfaceById(id)
            .orElseThrow { WebApplicationException(Response.Status.NOT_FOUND) }
    }

    @GET
    @Path("loads/{id}")
    fun networkInterfaceLoadById(
        @PathParam("id") id: String
    ): NetworkInterfaceLoad {
        return infoProvider.networkInterfaceLoadById(id)
            .orElseThrow { WebApplicationException(Response.Status.NOT_FOUND) }
    }

    @GET
    @Path("loads/history")
    fun getLoadHistory(
        @QueryParam("fromDate") fromDate: OffsetDateTime?,
        @QueryParam("toDate") toDate: OffsetDateTime?
    ): List<HistoryEntry<List<NetworkInterfaceLoad>>> {
        return historyManager.networkInterfaceLoadHistory(
            fromDate,
            toDate
        )
    }
}