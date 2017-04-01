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
package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.domain.storage.DiskHealth;

import java.util.Map;
import java.util.Optional;

public interface InfoProvider {

    DiskHealth diskHealth(String name);

    double[] cpuTemperatures();

    double cpuFanRpm();

    double cpuFanPercent();

    HealthData[] mainboardHealthData();

    Gpu[] gpus();

    Map<String, GpuHealth> gpuHealths();

    CpuLoad cpuLoad();

    NetworkInterfaceData[] getAllNetworkInterfaces();

    Optional<NetworkInterfaceData> getNetworkInterfaceById(String id);

    Optional<NetworkInterfaceSpeed> getNetworkInterfaceSpeed(String id);

    String[] getNetworkInterfaceNames();
}
