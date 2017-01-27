package com.krillsson.sysapi.dto.cpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "centralProcessor",
        "processCount",
        "threadCount",
        "cpuHealth"
})
public class CpuInfo {

    @JsonProperty("centralProcessor")
    private CentralProcessor centralProcessor;
    @JsonProperty("processCount")
    private Integer processCount;
    @JsonProperty("threadCount")
    private Integer threadCount;
    @JsonProperty("cpuHealth")
    private CpuHealth cpuHealth;

    /**
     * No args constructor for use in serialization
     */
    public CpuInfo() {
    }

    /**
     * @param centralProcessor
     * @param threadCount
     * @param cpuHealth
     * @param processCount
     */
    public CpuInfo(CentralProcessor centralProcessor, Integer processCount, Integer threadCount, CpuHealth cpuHealth) {
        super();
        this.centralProcessor = centralProcessor;
        this.processCount = processCount;
        this.threadCount = threadCount;
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
    public Integer getProcessCount() {
        return processCount;
    }

    @JsonProperty("processCount")
    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }

    @JsonProperty("threadCount")
    public Integer getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
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
