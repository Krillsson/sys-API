package com.krillsson.sysapi.dto.system;

import com.krillsson.sysapi.dto.cpu.CpuInfo;
import com.krillsson.sysapi.dto.drives.Drive;
import com.krillsson.sysapi.dto.gpu.Gpu;
import com.krillsson.sysapi.dto.memory.MemoryLoad;
import com.krillsson.sysapi.dto.motherboard.Motherboard;
import com.krillsson.sysapi.dto.network.NetworkInterface;
import com.krillsson.sysapi.dto.processes.Process;

import java.util.List;


public class SystemInfo {
    private String hostName;
    private OperatingSystem operatingSystem;
    private PlatformEnum platform;
    private CpuInfo cpuInfo;
    private Motherboard motherboard;
    private MemoryLoad memory;
    private List<Drive> drives;
    private List<NetworkInterface> networkInterfaces;
    private List<Gpu> gpus;

    public SystemInfo(String hostName, OperatingSystem operatingSystem, PlatformEnum platform, CpuInfo cpuInfo, Motherboard motherboard, MemoryLoad memory, List<Drive> drives, List<NetworkInterface> networkInterfaces, List<Gpu> gpus) {
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

    public SystemInfo() {
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CpuInfo cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public MemoryLoad getMemory() {
        return memory;
    }

    public void setMemory(MemoryLoad memory) {
        this.memory = memory;
    }

    public List<Drive> getDrives() {
        return drives;
    }

    public void setDrives(List<Drive> drives) {
        this.drives = drives;
    }

    public List<NetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setNetworkInterfaces(List<NetworkInterface> networkInterfaces) {
        this.networkInterfaces = networkInterfaces;
    }

    public List<Gpu> getGpus() {
        return gpus;
    }

    public void setGpus(List<Gpu> gpus) {
        this.gpus = gpus;
    }

}
