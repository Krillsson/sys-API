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
import com.krillsson.sysapi.core.domain.system.SystemInfoMapper
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.core.metrics.DriveMetrics
import com.krillsson.sysapi.core.metrics.GpuMetrics
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.MotherboardMetrics
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.dto.history.HistoryEntry
import com.krillsson.sysapi.dto.system.JvmProperties
import com.krillsson.sysapi.dto.system.SystemInfo
import com.krillsson.sysapi.dto.system.SystemLoad
import com.krillsson.sysapi.util.EnvironmentUtils
import io.dropwizard.auth.Auth
import org.slf4j.LoggerFactory
import oshi.PlatformEnum
import oshi.software.os.OperatingSystem
import oshi.software.os.OperatingSystem.ProcessSort
import java.time.OffsetDateTime
import java.util.Arrays
import java.util.HashMap
import java.util.Optional
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
class SystemResource(
    private val operatingSystem: OperatingSystem,
    private val platformEnum: PlatformEnum,
    private val cpuMetrics: CpuMetrics,
    private val networkMetrics: NetworkMetrics,
    private val driveMetrics: DriveMetrics,
    private val memoryMetrics: MemoryMetrics,
    private val processesMetrics: ProcessesMetrics,
    private val gpuMetrics: GpuMetrics,
    private val motherboardMetrics: MotherboardMetrics,
    private val historyManager: MetricsHistoryManager,
    private val uptimeSupplier: Supplier<Long>
) {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getRoot(@Auth user: UserConfiguration?): SystemInfo {
        return SystemInfoMapper.INSTANCE.map(
            com.krillsson.sysapi.core.domain.system.SystemInfo(
                EnvironmentUtils.getHostName(),
                operatingSystem,
                platformEnum,
                cpuMetrics.cpuInfo(),
                motherboardMetrics.motherboard(),
                memoryMetrics.memoryLoad(),
                driveMetrics.drives(),
                networkMetrics.networkInterfaces(),
                gpuMetrics.gpus()
            )
        )
    }

    @GET
    @Path("load")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getLoad(
        @Auth user: UserConfiguration?,
        @QueryParam("sortBy") processSort: Optional<String>,
        @QueryParam("limit") limit: Optional<Int>
    ): SystemLoad {
        var sortBy = DEFAULT_PROCESS_ORDER
        if (processSort.isPresent) {
            val method = processSort.get().toUpperCase()
            sortBy = try {
                ProcessSort.valueOf(method)
            } catch (e: IllegalArgumentException) {
                val validOptions =
                    Arrays.stream(ProcessSort.values())
                        .map { obj: ProcessSort -> obj.name }
                        .collect(Collectors.joining(", ", "Valid options are: ", "."))
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
        return SystemInfoMapper.INSTANCE.map(
            com.krillsson.sysapi.core.domain.system.SystemLoad(
                uptimeSupplier.get(),
                cpuMetrics.cpuLoad().systemLoadAverage,
                cpuMetrics.cpuLoad(),
                networkMetrics.networkInterfaceLoads(),
                driveMetrics.driveLoads(),
                memoryMetrics.memoryLoad(),
                processesMetrics.processesInfo(sortBy, theLimit).processes, gpuMetrics.gpuLoads(),
                motherboardMetrics.motherboardHealth()
            )
        )
    }

    @GET
    @Path("load/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getLoadHistory(
        @Auth user: UserConfiguration?,
        @QueryParam("fromDate") fromDate: OffsetDateTime?,
        @QueryParam("toDate") toDate: OffsetDateTime?
    ): List<HistoryEntry<SystemLoad>> {
        return SystemInfoMapper.INSTANCE.mapHistory(historyManager.systemLoadHistory(fromDate, toDate))
    }

    @GET
    @Path("uptime")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getUptime(@Auth user: UserConfiguration?): Long {
        return uptimeSupplier.get()
    }

    @GET
    @Path("jvm")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    fun getProperties(@Auth user: UserConfiguration?): JvmProperties {
        val propertiesMap: MutableMap<String, String> =
            HashMap()
        val systemProperties = System.getProperties()
        for ((key, value) in systemProperties) {
            propertiesMap[key as String] = value as String
        }
        return SystemInfoMapper.INSTANCE.map(com.krillsson.sysapi.core.domain.system.JvmProperties(propertiesMap))
    }

    companion object {
        private val LOGGER =
            LoggerFactory.getLogger(SystemResource::class.java)
        private const val DEFAULT_PROCESS_LIMIT = 10
        private val DEFAULT_PROCESS_ORDER = ProcessSort.MEMORY
    }
}