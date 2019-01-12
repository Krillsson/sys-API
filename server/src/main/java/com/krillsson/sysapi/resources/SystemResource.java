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
package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.domain.system.JvmProperties;
import com.krillsson.sysapi.core.domain.system.SystemInfo;
import com.krillsson.sysapi.core.domain.system.SystemInfoMapper;
import com.krillsson.sysapi.core.history.MetricsHistoryManager;
import com.krillsson.sysapi.core.metrics.*;
import com.krillsson.sysapi.dto.history.HistoryEntry;
import com.krillsson.sysapi.dto.system.SystemLoad;
import com.krillsson.sysapi.util.EnvironmentUtils;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import oshi.PlatformEnum;
import oshi.software.os.OperatingSystem;

import javax.annotation.Nullable;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SystemResource.class);

    private static final int DEFAULT_PROCESS_LIMIT = 10;
    private static final OperatingSystem.ProcessSort DEFAULT_PROCESS_ORDER = OperatingSystem.ProcessSort.MEMORY;

    private final OperatingSystem operatingSystem;
    private final PlatformEnum platformEnum;
    private final CpuMetrics cpuMetrics;
    private final NetworkMetrics networkMetrics;
    private final DriveMetrics driveMetrics;
    private final MemoryMetrics memoryMetrics;
    private final ProcessesMetrics processesMetrics;
    private final GpuMetrics gpuMetrics;
    private final MotherboardMetrics motherboardMetrics;
    private final MetricsHistoryManager historyManager;
    private final Supplier<Long> uptimeSupplier;


    public SystemResource(OperatingSystem operatingSystem, PlatformEnum platformEnum, CpuMetrics cpuMetrics,
                          NetworkMetrics networkMetrics,
                          DriveMetrics driveMetrics,
                          MemoryMetrics memoryMetrics,
                          ProcessesMetrics processesMetrics, GpuMetrics gpuMetrics,
                          MotherboardMetrics motherboardMetrics,
                          MetricsHistoryManager historyManager,
                          Supplier<Long> uptimeSupplier) {
        this.operatingSystem = operatingSystem;
        this.platformEnum = platformEnum;
        this.cpuMetrics = cpuMetrics;
        this.networkMetrics = networkMetrics;
        this.driveMetrics = driveMetrics;
        this.memoryMetrics = memoryMetrics;
        this.processesMetrics = processesMetrics;
        this.gpuMetrics = gpuMetrics;
        this.motherboardMetrics = motherboardMetrics;
        this.historyManager = historyManager;
        this.uptimeSupplier = uptimeSupplier;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.system.SystemInfo getRoot(@Auth UserConfiguration user) {
        return SystemInfoMapper.INSTANCE.map(new SystemInfo(
                EnvironmentUtils.getHostName(),
                operatingSystem,
                platformEnum,
                cpuMetrics.cpuInfo(),
                motherboardMetrics.motherboard(),
                memoryMetrics.memoryLoad(),
                driveMetrics.drives(),
                networkMetrics.networkInterfaces(),
                gpuMetrics.gpus()
        ));
    }

    @GET
    @Path("load")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public SystemLoad getLoad(@Auth UserConfiguration user, @QueryParam("sortBy") Optional<String> processSort, @QueryParam("limit") Optional<Integer> limit) {
        OperatingSystem.ProcessSort sortBy = DEFAULT_PROCESS_ORDER;
        if (processSort.isPresent()) {
            String method = processSort.get().toUpperCase();
            try {
                sortBy = OperatingSystem.ProcessSort.valueOf(method);
            } catch (IllegalArgumentException e) {
                String validOptions = Arrays.stream(OperatingSystem.ProcessSort.values())
                        .map(Enum::name)
                        .collect(Collectors.joining(", ", "Valid options are: ", "."));
                LOGGER.error("No process sort method of type {} was found. {}", method, validOptions);
                throw new WebApplicationException(
                        String.format("No process sort method of type %s was found. %s", method, validOptions),
                        Response.Status.BAD_REQUEST
                );
            }
        }
        Integer theLimit = limit.orElse(DEFAULT_PROCESS_LIMIT);
        if (theLimit < -1) {
            String message = String.format("limit cannot be negative (%d)", theLimit);
            LOGGER.error(message);
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }

        return SystemInfoMapper.INSTANCE.map(new com.krillsson.sysapi.core.domain.system.SystemLoad(
                uptimeSupplier.get(), cpuMetrics.cpuLoad(),
                networkMetrics.networkInterfaceLoads(),
                driveMetrics.driveLoads(),
                memoryMetrics.memoryLoad(),
                processesMetrics.processesInfo(sortBy, theLimit).getProcesses(), gpuMetrics.gpuLoads(),
                motherboardMetrics.motherboardHealth()
        ));
    }


    @GET
    @Path("load/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<HistoryEntry<SystemLoad>> getLoadHistory(@Auth UserConfiguration user, @QueryParam("fromDate") ZonedDateTime fromDate, @QueryParam("toDate") ZonedDateTime toDate) {
        return SystemInfoMapper.INSTANCE.mapHistory(historyManager.systemLoadHistory(fromDate, toDate));
    }

    @GET
    @Path("uptime")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public long getUptime(@Auth UserConfiguration user) {
        return uptimeSupplier.get();
    }


    @GET
    @Path("jvm")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.system.JvmProperties getProperties(@Auth UserConfiguration user) {
        Map<String, String> propertiesMap = new HashMap<>();
        Properties systemProperties = java.lang.System.getProperties();
        for (Map.Entry<Object, Object> x : systemProperties.entrySet()) {
            propertiesMap.put((String) x.getKey(), (String) x.getValue());
        }
        return SystemInfoMapper.INSTANCE.map(new JvmProperties(propertiesMap));
    }


}
