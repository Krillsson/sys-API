package com.krillsson.maintenance.representation.cpu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cpu {
    private CpuInfo cpuInfo;
    private double systemUsagePercent;
    private CpuTime totalCpuTime;
    private List<CpuTime> cpuTimePerCore;

    public Cpu(CpuInfo cpuInfo, double systemUsagePercent, CpuTime totalCpuTime, List<CpuTime> cpuTimePerCore) {
        this.cpuInfo = cpuInfo;
        this.systemUsagePercent = systemUsagePercent;
        this.totalCpuTime = totalCpuTime;
        this.cpuTimePerCore = cpuTimePerCore;
    }

    @JsonProperty
    public CpuInfo getCpuInfo() {
        return cpuInfo;
    }

    @JsonProperty
    public double getSystemUsagePercent() {
        return systemUsagePercent;
    }

    @JsonProperty
    public CpuTime getTotalCpuTime() {
        return totalCpuTime;
    }

    @JsonProperty
    public List<CpuTime> getCpuTimePerCore() {
        return cpuTimePerCore;
    }
}
