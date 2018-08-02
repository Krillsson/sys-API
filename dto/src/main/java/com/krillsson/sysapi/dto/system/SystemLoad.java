package com.krillsson.sysapi.dto.system;

import com.krillsson.sysapi.dto.cpu.CpuLoad;
import com.krillsson.sysapi.dto.drives.DriveLoad;
import com.krillsson.sysapi.dto.gpu.GpuLoad;
import com.krillsson.sysapi.dto.memory.GlobalMemory;
import com.krillsson.sysapi.dto.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.dto.sensors.HealthData;

import java.util.List;

public class SystemLoad {
    private CpuLoad cpuLoad;
    private List<NetworkInterfaceLoad> networkInterfaceLoads;
    private List<DriveLoad> driveLoads;
    private GlobalMemory memory;
    private List<GpuLoad> gpuLoads;
    private List<HealthData> motherboardHealth;

    public SystemLoad(CpuLoad cpuLoad, List<NetworkInterfaceLoad> networkInterfaceLoads, List<DriveLoad> driveLoads, GlobalMemory memory, List<GpuLoad> gpuLoads, List<HealthData> motherboardHealth) {
        this.cpuLoad = cpuLoad;
        this.networkInterfaceLoads = networkInterfaceLoads;
        this.driveLoads = driveLoads;
        this.memory = memory;
        this.gpuLoads = gpuLoads;
        this.motherboardHealth = motherboardHealth;
    }

    public SystemLoad() {
    }

    public CpuLoad getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(CpuLoad cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public List<NetworkInterfaceLoad> getNetworkInterfaceLoads() {
        return networkInterfaceLoads;
    }

    public void setNetworkInterfaceLoads(List<NetworkInterfaceLoad> networkInterfaceLoads) {
        this.networkInterfaceLoads = networkInterfaceLoads;
    }

    public List<DriveLoad> getDriveLoads() {
        return driveLoads;
    }

    public void setDriveLoads(List<DriveLoad> driveLoads) {
        this.driveLoads = driveLoads;
    }

    public GlobalMemory getMemory() {
        return memory;
    }

    public void setMemory(GlobalMemory memory) {
        this.memory = memory;
    }

    public List<GpuLoad> getGpuLoads() {
        return gpuLoads;
    }

    public void setGpuLoads(List<GpuLoad> gpuLoads) {
        this.gpuLoads = gpuLoads;
    }

    public List<HealthData> getMotherboardHealth() {
        return motherboardHealth;
    }

    public void setMotherboardHealth(List<HealthData> motherboardHealth) {
        this.motherboardHealth = motherboardHealth;
    }
}
