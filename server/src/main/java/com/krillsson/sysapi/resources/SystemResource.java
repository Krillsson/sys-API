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
import oshi.PlatformEnum;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {

    private final PlatformEnum platformEnum;
    private final CpuMetrics cpuMetrics;
    private final NetworkMetrics networkMetrics;
    private final DriveMetrics driveMetrics;
    private final MemoryMetrics memoryMetrics;
    private final GpuMetrics gpuMetrics;
    private final MotherboardMetrics motherboardMetrics;
    private final MetricsHistoryManager historyManager;
    private final Supplier<Long> uptimeSupplier;


    public SystemResource(PlatformEnum platformEnum, CpuMetrics cpuMetrics,
                          NetworkMetrics networkMetrics,
                          DriveMetrics driveMetrics,
                          MemoryMetrics memoryMetrics,
                          GpuMetrics gpuMetrics,
                          MotherboardMetrics motherboardMetrics, MetricsHistoryManager historyManager, Supplier<Long> uptimeSupplier) {
        this.platformEnum = platformEnum;
        this.cpuMetrics = cpuMetrics;
        this.networkMetrics = networkMetrics;
        this.driveMetrics = driveMetrics;
        this.memoryMetrics = memoryMetrics;
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
                platformEnum,
                cpuMetrics.cpuInfo(),
                motherboardMetrics.motherboard(),
                memoryMetrics.globalMemory(),
                driveMetrics.drives(),
                networkMetrics.networkInterfaces(),
                gpuMetrics.gpus()
        ));
    }

    @GET
    @Path("load")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public SystemLoad getLoad(@Auth UserConfiguration user) {
        return SystemInfoMapper.INSTANCE.map(new com.krillsson.sysapi.core.domain.system.SystemLoad(
                cpuMetrics.cpuLoad(),
                networkMetrics.networkInterfaceLoads(),
                driveMetrics.driveLoads(),
                memoryMetrics.globalMemory(),
                gpuMetrics.gpuLoads(),
                motherboardMetrics.motherboardHealth()
        ));
    }


    @GET
    @Path("load/history")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<HistoryEntry<SystemLoad>> getLoadHistory(@Auth UserConfiguration user) {
        return SystemInfoMapper.INSTANCE.mapHistory(historyManager.systemLoadHistory());
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
