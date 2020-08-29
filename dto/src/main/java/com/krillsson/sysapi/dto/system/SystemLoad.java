package com.krillsson.sysapi.dto.system;

import com.krillsson.sysapi.dto.cpu.CpuLoad;
import com.krillsson.sysapi.dto.drives.DriveLoad;
import com.krillsson.sysapi.dto.gpu.GpuLoad;
import com.krillsson.sysapi.dto.memory.MemoryLoad;
import com.krillsson.sysapi.dto.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.dto.processes.Process;
import com.krillsson.sysapi.dto.sensors.HealthData;

import java.util.List;

public class SystemLoad {
    private long uptime;
    private double systemLoadAverage;
    private CpuLoad cpuLoad;
    private List<NetworkInterfaceLoad> networkInterfaceLoads;
    private List<DriveLoad> driveLoads;
    private MemoryLoad memory;
    private List<Process> processes;
    private List<GpuLoad> gpuLoads;
    private List<HealthData> motherboardHealth;

    public SystemLoad(long uptime, CpuLoad cpuLoad, List<NetworkInterfaceLoad> networkInterfaceLoads, List<DriveLoad> driveLoads, MemoryLoad memory, List<Process> processes, List<GpuLoad> gpuLoads, List<HealthData> motherboardHealth, double systemLoadAverage) {
        this.uptime = uptime;
        this.cpuLoad = cpuLoad;
        this.networkInterfaceLoads = networkInterfaceLoads;
        this.driveLoads = driveLoads;
        this.memory = memory;
        this.systemLoadAverage = systemLoadAverage;
        this.processes = processes;
        this.gpuLoads = gpuLoads;
        this.motherboardHealth = motherboardHealth;
    }

    public SystemLoad() {
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
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

    public MemoryLoad getMemory() {
        return memory;
    }

    public void setMemory(MemoryLoad memory) {
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

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
