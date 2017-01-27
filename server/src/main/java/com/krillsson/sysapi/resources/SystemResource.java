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
import com.krillsson.sysapi.core.InfoProvider;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.system.JvmProperties;
import com.krillsson.sysapi.core.domain.system.SystemInfo;
import com.krillsson.sysapi.core.domain.system.SystemInfoMapper;
import io.dropwizard.auth.Auth;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {

    private final OperatingSystem operatingSystem;
    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final PowerSource[] powerSources;
    private final Sensors sensors;
    private final InfoProvider provider;

    public SystemResource(InfoProvider provider, OperatingSystem operatingSystem, CentralProcessor processor, GlobalMemory memory, PowerSource[] powerSources, Sensors sensors) {
        this.provider = provider;
        this.operatingSystem = operatingSystem;
        this.processor = processor;
        this.memory = memory;
        this.powerSources = powerSources;
        this.sensors = sensors;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.system.SystemInfo getRoot(@Auth UserConfiguration user) {
        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.cpuFanRpm();
        double fanPercent = provider.cpuFanPercent();
        if (temperature.length == 0) {
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        return SystemInfoMapper.INSTANCE.map(new SystemInfo(
                getHostName(),
                operatingSystem,
                new CpuInfo(processor,
                        operatingSystem.getProcessCount(),
                        operatingSystem.getThreadCount(),
                        new CpuHealth(
                                temperature,
                                sensors.getCpuVoltage(),
                                fanRpm,
                                fanPercent)),
                memory,
                powerSources));
    }

    private String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "";
        }
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
