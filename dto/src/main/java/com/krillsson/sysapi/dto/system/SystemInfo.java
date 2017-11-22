package com.krillsson.sysapi.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.krillsson.sysapi.dto.cpu.CpuInfo;
import com.krillsson.sysapi.dto.power.PowerSource;
import com.krillsson.sysapi.dto.processes.Memory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hostName",
        "platform",
        "operatingSystem",
        "cpuInfo",
        "memory",
        "powerSources"
})
public class SystemInfo {

    @JsonProperty("hostName")
    private String hostName;
    @JsonProperty("platform")
    private PlatformEnum platform;
    @JsonProperty("operatingSystem")
    private OperatingSystem operatingSystem;
    @JsonProperty("cpuInfo")
    private CpuInfo cpuInfo;
    @JsonProperty("memory")
    private Memory memory;
    @JsonProperty("powerSources")
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
    public SystemInfo(String hostName, PlatformEnum platform,OperatingSystem operatingSystem, CpuInfo cpuInfo, Memory memory, PowerSource[] powerSources) {
        super();
        this.hostName = hostName;
        this.platform = platform;
        this.operatingSystem = operatingSystem;
        this.cpuInfo = cpuInfo;
        this.memory = memory;
        this.powerSources = powerSources;
    }

    @JsonProperty("hostName")
    public String getHostName() {
        return hostName;
    }

    @JsonProperty("hostName")
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @JsonProperty("platform")
    public PlatformEnum getPlatform() {
        return platform;
    }

    @JsonProperty("platform")
    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }

    @JsonProperty("operatingSystem")
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    @JsonProperty("operatingSystem")
    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    @JsonProperty("cpuInfo")
    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }

    @JsonProperty("cpuInfo")
    public void setCpuInfo(CpuInfo cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    @JsonProperty("memory")
    public Memory getMemory() {
        return memory;
    }

    @JsonProperty("memory")
    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    @JsonProperty("powerSources")
    public PowerSource[] getPowerSources() {
        return powerSources;
    }

    @JsonProperty("powerSources")
    public void setPowerSources(PowerSource[] powerSources) {
        this.powerSources = powerSources;
    }

}
