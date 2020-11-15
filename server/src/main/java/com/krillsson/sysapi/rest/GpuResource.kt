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
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.GpuMetrics
import java.time.OffsetDateTime
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("gpus")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
class GpuResource(
    private val provider: GpuMetrics,
    private val historyManager: MetricsHistoryManager
) {
    @GET
    fun getRoot(): List<Gpu> {
        return provider.gpus()
    }

    @GET
    @Path("loads")
    fun getLoad(): List<GpuLoad> {
        return provider.gpuLoads()
    }

    @GET
    @Path("loads/history")
    fun getLoadHistory(
        @QueryParam("fromDate") fromDate: OffsetDateTime?,
        @QueryParam("toDate") toDate: OffsetDateTime?
    ): List<HistoryEntry<List<GpuLoad>>> {
        return historyManager.gpuLoadHistory(fromDate, toDate)
    }
}