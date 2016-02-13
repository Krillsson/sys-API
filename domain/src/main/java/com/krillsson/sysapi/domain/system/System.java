package com.krillsson.sysapi.domain.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.domain.filesystem.Drive;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class System {
    private final String hostname;
    private final double uptime;
    private final String osName, osVersion;
    private final CpuLoad totalCpuLoad;
    private final MainMemory mainMemory;
    private final ProcessStatistics processStatistics;
    private NetworkInterfaceConfig mainNetworkInterface;
    private Drive mainFileSystem;

    public System(String hostname,
                  double uptime,
                  String osName,
                  String osVersion,
                  CpuLoad totalCpuLoad,
                  MainMemory mainMemory,
                  ProcessStatistics processStatistics) {
        this.hostname = hostname;
        this.uptime = uptime;
        this.osName = osName;
        this.osVersion = osVersion;
        this.totalCpuLoad = totalCpuLoad;
        this.mainMemory = mainMemory;
        this.processStatistics = processStatistics;
    }

    public System(String hostname,
                  double uptime,
                  String osName,
                  String osVersion,
                  CpuLoad totalCpuLoad,
                  MainMemory mainMemory,
                  ProcessStatistics processStatistics,
                  Drive mainFileSystem,
                  NetworkInterfaceConfig mainNetworkInterface) {
        this.mainFileSystem = mainFileSystem;
        this.mainNetworkInterface = mainNetworkInterface;
        this.processStatistics = processStatistics;
        this.mainMemory = mainMemory;
        this.totalCpuLoad = totalCpuLoad;
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
    public CpuLoad getTotalCpuLoad() {
        return totalCpuLoad;
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
    public Drive getMainFileSystem() {
        return mainFileSystem;
    }
}
