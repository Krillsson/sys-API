package com.krillsson.sysapi.dto.system;

import com.krillsson.sysapi.dto.cpu.CpuInfo;
import com.krillsson.sysapi.dto.power.PowerSource;
import com.krillsson.sysapi.dto.processes.Memory;


public class SystemInfo {


    private String hostName;

    private PlatformEnum platform;

    private OperatingSystem operatingSystem;

    private CpuInfo cpuInfo;

    private Memory memory;

    private PowerSource[] powerSources = null;

    /**
     * No args constructor for use in serialization
     */
    public SystemInfo() {
    }

    /**
     * @param operatingSystem
     * @param cpuInfo
     * @param hostName
     * @param powerSources
     * @param memory
     */
    public SystemInfo(String hostName, PlatformEnum platform, OperatingSystem operatingSystem, CpuInfo cpuInfo, Memory memory, PowerSource[] powerSources) {
        super();
        this.hostName = hostName;
        this.platform = platform;
        this.operatingSystem = operatingSystem;
        this.cpuInfo = cpuInfo;
        this.memory = memory;
        this.powerSources = powerSources;
    }


    public String getHostName() {
        return hostName;
    }


    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    public PlatformEnum getPlatform() {
        return platform;
    }


    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }


    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }


    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }


    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }


    public void setCpuInfo(CpuInfo cpuInfo) {
        this.cpuInfo = cpuInfo;
    }


    public Memory getMemory() {
        return memory;
    }


    public void setMemory(Memory memory) {
        this.memory = memory;
    }


    public PowerSource[] getPowerSources() {
        return powerSources;
    }


    public void setPowerSources(PowerSource[] powerSources) {
        this.powerSources = powerSources;
    }

}
