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
package com.krillsson.sysapi.core.domain.sensors;

import com.krillsson.sysapi.core.domain.cpu.CpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;

import java.util.Map;

public class SensorsInfo {
    private final CpuHealth cpuHealth;
    private final Map<String, GpuHealth> gpuHealths;
    private final HealthData[] healthData;

    public SensorsInfo(CpuHealth cpuHealth, Map<String, GpuHealth> gpuHealths, HealthData[] healthData) {
        this.cpuHealth = cpuHealth;
        this.gpuHealths = gpuHealths;
        this.healthData = healthData;
    }

    public Map<String, GpuHealth> getGpuHealths() {
        return gpuHealths;
    }

    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }

    public HealthData[] getHealthData() {
        return healthData;
    }
}
