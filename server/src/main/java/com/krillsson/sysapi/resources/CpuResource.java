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
import com.krillsson.sysapi.core.domain.cpu.CpuInfoMapper;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import io.dropwizard.auth.Auth;
import oshi.hardware.CentralProcessor;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("cpu")
@Produces(MediaType.APPLICATION_JSON)
public class CpuResource {

    private final OperatingSystem operatingSystem;
    private final Sensors sensors;
    private final CentralProcessor processor;
    private final InfoProvider provider;

    public CpuResource(OperatingSystem operatingSystem, Sensors sensors, CentralProcessor processor, InfoProvider provider) {
        this.operatingSystem = operatingSystem;
        this.sensors = sensors;
        this.processor = processor;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public com.krillsson.sysapi.dto.cpu.CpuInfo getRoot(@Auth UserConfiguration user) {

        double[] temperature = provider.cpuTemperatures();
        double fanRpm = provider.cpuFanRpm();
        double fanPercent = provider.cpuFanPercent();
        CpuLoad cpuLoad = provider.cpuLoad();
        if (temperature.length == 0) {
            temperature = new double[]{sensors.getCpuTemperature()};
        }
        return CpuInfoMapper.INSTANCE.map(new CpuInfo(processor,
                operatingSystem.getProcessCount(),
                operatingSystem.getThreadCount(),
                cpuLoad, new CpuHealth(temperature,
                sensors.getCpuVoltage(),
                fanRpm,
                fanPercent)));
    }

    @GET
    @Path("ticks")
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public long[] getTicks(@Auth UserConfiguration user) {
        return processor.getSystemCpuLoadTicks();
    }

}
