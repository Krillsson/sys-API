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
import com.krillsson.sysapi.core.domain.processes.ProcessInfoMapper
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.dto.processes.Process
import com.krillsson.sysapi.dto.processes.ProcessInfo
import io.dropwizard.auth.Auth
import org.slf4j.LoggerFactory
import java.util.Optional
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("processes")
@Produces(MediaType.APPLICATION_JSON)
class ProcessesResource(private val provider: ProcessesMetrics) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getRoot(
        @Auth user: UserConfiguration?,
        @QueryParam("sortBy") processSort: Optional<String>,
        @QueryParam("limit") limit: Optional<Int>
    ): ProcessInfo {
        var sortBy = DEFAULT_PROCESS_ORDER
        if (processSort.isPresent) {
            val method = processSort.get().toUpperCase()
            sortBy = try {
                ProcessSort.valueOf(method)
            } catch (e: IllegalArgumentException) {
                val validOptions =
                    ProcessSort.values()
                        .joinToString(separator = ",", prefix = "Valid options are: ") { it.name }
                LOGGER.error(
                    "No process sort method of type {} was found. {}",
                    method,
                    validOptions
                )
                throw WebApplicationException(
                    String.format("No process sort method of type %s was found. %s", method, validOptions),
                    Response.Status.BAD_REQUEST
                )
            }
        }
        val theLimit = limit.orElse(DEFAULT_PROCESS_LIMIT)
        if (theLimit < -1) {
            val message = String.format("limit cannot be negative (%d)", theLimit)
            LOGGER.error(message)
            throw WebApplicationException(message, Response.Status.BAD_REQUEST)
        }
        val value = provider.processesInfo(sortBy, theLimit)
        return ProcessInfoMapper.INSTANCE.map(value)
    }

    @GET
    @Path("{pid}")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getProcessByPid(@PathParam("pid") pid: Int): Process? {
        val process =
            provider.getProcessByPid(pid)
        if (!process.isPresent) {
            throw WebApplicationException(
                String.format("No process with PID %d was found.", pid),
                Response.Status.NOT_FOUND
            )
        }
        return ProcessInfoMapper.INSTANCE.map(process.get())
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ProcessesResource::class.java)
        private const val DEFAULT_PROCESS_LIMIT = 10
        private val DEFAULT_PROCESS_ORDER = ProcessSort.MEMORY
    }
}