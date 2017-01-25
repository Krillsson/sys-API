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
import com.krillsson.sysapi.core.domain.health.HealthData;
import com.krillsson.sysapi.core.domain.health.SensorsData;
import io.dropwizard.auth.Auth;
import oshi.hardware.Sensors;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorsResource {

    private final Sensors sensors;
    private InfoProvider provider;

    public SensorsResource(Sensors sensors, InfoProvider provider) {
        this.sensors = sensors;
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public SensorsData getRoot(@Auth UserConfiguration user) {
        double[] cpuTemperatures = provider.cpuTemperatures();
        double cpuFanRpm = provider.cpuFanRpm();
        double cpuFanPercent = provider.cpuFanPercent();
        if (cpuTemperatures.length == 0) {
            cpuTemperatures = new double[]{sensors.getCpuTemperature()};
        }
        HealthData[] healthDatas = provider.healthData();
        return new SensorsData(new CpuHealth(cpuTemperatures, sensors.getCpuVoltage(), cpuFanRpm, cpuFanPercent), provider.gpuHealths(), healthDatas);
    }

}