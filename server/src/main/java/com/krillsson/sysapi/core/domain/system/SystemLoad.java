package com.krillsson.sysapi.core.domain.system;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import oshi.hardware.GlobalMemory;

import java.util.List;

public class SystemLoad {
    private final CpuLoad cpuLoad;
    private final List<NetworkInterfaceLoad> networkInterfaceLoads;
    private final List<DriveLoad> driveLoads;
    private final GlobalMemory memory;
    private final List<GpuLoad> gpuLoads;
    private final List<HealthData> motherboardHealth;

    public SystemLoad(CpuLoad cpuLoad, List<NetworkInterfaceLoad> networkInterfaceLoads, List<DriveLoad> driveLoads, GlobalMemory memory, List<GpuLoad> gpuLoads, List<HealthData> motherboardHealth) {
        this.cpuLoad = cpuLoad;
        this.networkInterfaceLoads = networkInterfaceLoads;
        this.driveLoads = driveLoads;
        this.memory = memory;
        this.gpuLoads = gpuLoads;
        this.motherboardHealth = motherboardHealth;
    }

    public CpuLoad getCpuLoad() {
        return cpuLoad;
    }

    public List<NetworkInterfaceLoad> getNetworkInterfaceLoads() {
        return networkInterfaceLoads;
    }

    public List<DriveLoad> getDriveLoads() {
        return driveLoads;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public List<GpuLoad> getGpuLoads() {
        return gpuLoads;
    }

    public List<HealthData> getMotherboardHealth() {
        return motherboardHealth;
    }
}
