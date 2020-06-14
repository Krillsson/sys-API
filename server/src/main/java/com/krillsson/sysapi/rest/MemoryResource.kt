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
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.domain.memory.MemoryMapper
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.dto.history.HistoryEntry
import com.krillsson.sysapi.dto.memory.MemoryLoad
import io.dropwizard.auth.Auth
import java.time.OffsetDateTime
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("memory")
@Produces(MediaType.APPLICATION_JSON)
class MemoryResource(
    private val provider: MemoryMetrics,
    private val historyManager: MetricsHistoryManager
) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getRoot(@Auth user: UserConfiguration?): MemoryLoad? {
        return MemoryMapper.INSTANCE.map(provider.memoryLoad())
    }

    @GET
    @Path("history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getLoadHistory(
        @Auth user: UserConfiguration?,
        @QueryParam("fromDate") fromDate: OffsetDateTime?,
        @QueryParam("toDate") toDate: OffsetDateTime?
    ): List<HistoryEntry<MemoryLoad>> {
        return MemoryMapper.INSTANCE.mapHistory(historyManager.memoryHistory(fromDate, toDate))
    }
}