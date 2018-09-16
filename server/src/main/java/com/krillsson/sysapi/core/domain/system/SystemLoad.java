package com.krillsson.sysapi.core.domain.system;

import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.processes.Process;
import com.krillsson.sysapi.core.domain.sensors.HealthData;

import java.util.List;

public class SystemLoad {
    private final long uptime;
    private final CpuLoad cpuLoad;
    private final List<NetworkInterfaceLoad> networkInterfaceLoads;
    private final List<DriveLoad> driveLoads;
    private final MemoryLoad memory;
    private final List<Process> processes;
    private final List<GpuLoad> gpuLoads;
    private final List<HealthData> motherboardHealth;

    public SystemLoad(long uptime, CpuLoad cpuLoad, List<NetworkInterfaceLoad> networkInterfaceLoads, List<DriveLoad> driveLoads, MemoryLoad memory, List<Process> processes, List<GpuLoad> gpuLoads, List<HealthData> motherboardHealth) {
        this.uptime = uptime;
        this.cpuLoad = cpuLoad;
        this.networkInterfaceLoads = networkInterfaceLoads;
        this.driveLoads = driveLoads;
        this.memory = memory;
        this.processes = processes;
        this.gpuLoads = gpuLoads;
        this.motherboardHealth = motherboardHealth;
    }

    public long getUptime() {
        return uptime;
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

    public MemoryLoad getMemory() {
        return memory;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public List<GpuLoad> getGpuLoads() {
        return gpuLoads;
    }

    public List<HealthData> getMotherboardHealth() {
        return motherboardHealth;
    }
}
