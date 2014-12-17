package se.christianjensen.maintenance.representation.cpu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cpu {
    private CpuInfo cpuInfo;
    private double systemUsagePercent;
    private List<CpuTime> cpuTime;

    public Cpu(CpuInfo cpuInfo, double systemUsagePercent, List<CpuTime> cpuTime) {
        this.cpuInfo = cpuInfo;
        this.systemUsagePercent = systemUsagePercent;
        this.cpuTime = cpuTime;
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
    public List<CpuTime> getCpuTime() {
        return cpuTime;
    }
}
