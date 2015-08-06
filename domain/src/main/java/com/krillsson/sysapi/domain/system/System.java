package com.krillsson.sysapi.domain.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krillsson.sysapi.domain.cpu.CpuTime;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;

public class System {
    private String hostname;
    private double uptime;
    private String osName, osVersion;
    private CpuTime totalCpuTime;
    private MainMemory mainMemory;
    private ProcessStatistics processStatistics;

    public System(String hostname,
                  double uptime,
                  String osName,
                  String osVersion,
                  CpuTime totalCpuTime,
                  MainMemory mainMemory,
                  ProcessStatistics processStatistics) {
        this.hostname = hostname;
        this.uptime = uptime;
        this.osName = osName;
        this.osVersion = osVersion;
        this.totalCpuTime = totalCpuTime;
        this.mainMemory = mainMemory;
        this.processStatistics = processStatistics;
    }

    @JsonProperty
    public String getHostname() {
        return hostname;
    }

    @JsonProperty
    public double getUptime() {
        return uptime;
    }

    @JsonProperty
    public String getOsName() {
        return osName;
    }

    @JsonProperty
    public String getOsVersion() {
        return osVersion;
    }

    @JsonProperty
    public CpuTime getTotalCpuTime() {
        return totalCpuTime;
    }

    @JsonProperty
    public ProcessStatistics getProcessStatistics() {
        return processStatistics;
    }

    @JsonProperty
    public MainMemory getMainMemory() {
        return mainMemory;
    }
}
