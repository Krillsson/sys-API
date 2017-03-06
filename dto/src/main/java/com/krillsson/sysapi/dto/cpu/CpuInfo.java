package com.krillsson.sysapi.dto.cpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "centralProcessor",
        "processCount",
        "threadCount",
        "cpuLoad",
        "cpuHealth"
})
public class CpuInfo {

    @JsonProperty("centralProcessor")
    private CentralProcessor centralProcessor;
    @JsonProperty("processCount")
    private int processCount;
    @JsonProperty("threadCount")
    private int threadCount;
    @JsonProperty("cpuLoad")
    private CpuLoad cpuLoad;
    @JsonProperty("cpuHealth")
    private CpuHealth cpuHealth;

    /**
     * No args constructor for use in serialization
     */
    public CpuInfo() {
    }

    /**
     * @param centralProcessor
     * @param processCount
     * @param threadCount
     * @param cpuLoad
     * @param cpuHealth
     */
    public CpuInfo(CentralProcessor centralProcessor, int processCount, int threadCount, CpuLoad cpuLoad, CpuHealth cpuHealth) {
        super();
        this.centralProcessor = centralProcessor;
        this.processCount = processCount;
        this.threadCount = threadCount;
        this.cpuLoad = cpuLoad;
        this.cpuHealth = cpuHealth;
    }

    @JsonProperty("centralProcessor")
    public CentralProcessor getCentralProcessor() {
        return centralProcessor;
    }

    @JsonProperty("centralProcessor")
    public void setCentralProcessor(CentralProcessor centralProcessor) {
        this.centralProcessor = centralProcessor;
    }

    @JsonProperty("processCount")
    public int getProcessCount() {
        return processCount;
    }

    @JsonProperty("processCount")
    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    @JsonProperty("threadCount")
    public int getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty("cpuLoad")
    public CpuLoad getCpuLoad()
    {
        return cpuLoad;
    }

    @JsonProperty("cpuLoad")
    public void setCpuLoad(CpuLoad cpuLoad)
    {
        this.cpuLoad = cpuLoad;
    }

    @JsonProperty("cpuHealth")
    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }

    @JsonProperty("cpuHealth")
    public void setCpuHealth(CpuHealth cpuHealth) {
        this.cpuHealth = cpuHealth;
    }

}
