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

import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.network.NetworkInterface;
import oshi.PlatformEnum;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import java.util.List;

public class SystemInfo {
    private final String hostName;
    private final OperatingSystem operatingSystem;
    private final PlatformEnum platform;
    private final CpuInfo cpuInfo;
    private final Motherboard motherboard;
    private final GlobalMemory memory;
    private final List<Drive> drives;
    private final List<NetworkInterface> networkInterfaces;
    private final List<Gpu> gpus;

    public SystemInfo(String hostName, OperatingSystem operatingSystem, PlatformEnum platform, CpuInfo cpuInfo, Motherboard motherboard, GlobalMemory memory, List<Drive> drives, List<NetworkInterface> networkInterfaces, List<Gpu> gpus) {
        this.hostName = hostName;
        this.operatingSystem = operatingSystem;
        this.platform = platform;
        this.cpuInfo = cpuInfo;
        this.motherboard = motherboard;
        this.memory = memory;
        this.drives = drives;
        this.networkInterfaces = networkInterfaces;
        this.gpus = gpus;
    }

    public String getHostName() {
        return hostName;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public List<Drive> getDrives() {
        return drives;
    }

    public List<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public List<Gpu> getGpus() {
        return gpus;
    }
}
