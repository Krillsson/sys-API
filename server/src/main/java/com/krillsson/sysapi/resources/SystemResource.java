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
import com.krillsson.sysapi.core.domain.cpu.Cpu;
import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.system.JvmProperties;
import com.krillsson.sysapi.core.domain.system.System;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.*;
import oshi.json.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.UnknownHostException;
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
    public System getRoot(@Auth UserConfiguration user) {
        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.cpuFanRpm();
        double fanPercent = provider.cpuFanPercent();
        if (temperature.length == 0) {
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        return new System(
                getHostName(),
                operatingSystem,
                new Cpu(processor,
                        operatingSystem.getProcessCount(),
                        operatingSystem.getThreadCount(),
                        new CpuHealth(
                                temperature,
                                sensors.getCpuVoltage(),
                                fanRpm,
                                fanPercent)),
                memory,
                powerSources);
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
    public JvmProperties getProperties(@Auth UserConfiguration user) {
        Properties p = java.lang.System.getProperties();
        return new JvmProperties(
                p.getProperty("java.home"),
                p.getProperty("java.class.path"),
                p.getProperty("java.vendor"),
                p.getProperty("java.vendor.url"),
                p.getProperty("java.version"),
                p.getProperty("user.dir"),
                p.getProperty("user.home"),
                p.getProperty("user.name"));
    }

}
