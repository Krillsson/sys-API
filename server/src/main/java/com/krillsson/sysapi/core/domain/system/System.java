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
package com.krillsson.sysapi.core.domain.system;

import com.krillsson.sysapi.core.domain.cpu.Cpu;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PowerSource;
import oshi.software.os.OperatingSystem;

public class System {
    private final String hostName;
    private final OperatingSystem operatingSystem;
    private final Cpu cpu;
    private final GlobalMemory memory;
    private final PowerSource[] powerSources;

    public System(String hostName, OperatingSystem operatingSystem, Cpu cpu, GlobalMemory memory, PowerSource[] powerSources) {
        this.hostName = hostName;
        this.operatingSystem = operatingSystem;
        this.cpu = cpu;
        this.memory = memory;
        this.powerSources = powerSources;
    }

    public String getHostName() {
        return hostName;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public Cpu getCpu() {
        return cpu;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public PowerSource[] getPowerSources() {
        return powerSources;
    }
}
