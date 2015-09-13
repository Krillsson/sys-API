package com.krillsson.sysapi.domain.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krillsson.sysapi.domain.cpu.CpuTime;
import com.krillsson.sysapi.domain.filesystem.FileSystem;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;

public class System {
    private final String hostname;
    private final double uptime;
    private final String osName, osVersion;
    private final CpuTime totalCpuTime;
    private final MainMemory mainMemory;
    private final ProcessStatistics processStatistics;
    private NetworkInterfaceConfig mainNetworkInterface;
    private FileSystem mainFileSystem;

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

    public System(String hostname,
                  double uptime,
                  String osName,
                  String osVersion,
                  CpuTime totalCpuTime,
                  MainMemory mainMemory,
                  ProcessStatistics processStatistics,
                  FileSystem mainFileSystem,
                  NetworkInterfaceConfig mainNetworkInterface) {
        this.mainFileSystem = mainFileSystem;
        this.mainNetworkInterface = mainNetworkInterface;
        this.processStatistics = processStatistics;
        this.mainMemory = mainMemory;
        this.totalCpuTime = totalCpuTime;
        this.osVersion = osVersion;
        this.osName = osName;
        this.uptime = uptime;
        this.hostname = hostname;
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

    @JsonProperty
    public NetworkInterfaceConfig getMainNetworkInterface() {
        return mainNetworkInterface;
    }

    @JsonProperty
    public FileSystem getMainFileSystem() {
        return mainFileSystem;
    }
}
