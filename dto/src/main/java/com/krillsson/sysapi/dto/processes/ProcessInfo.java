package com.krillsson.sysapi.dto.processes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "memory",
        "processId",
        "threadCount",
        "processCount",
        "processes"
})
public class ProcessInfo {

    @JsonProperty("memory")
    private Memory memory;
    @JsonProperty("processes")
    private Process[] processes = null;
    @JsonProperty("processId")
    private long processId;
    @JsonProperty("threadCount")
    private long threadCount;
    @JsonProperty("processCount")
    private long processCount;

    /**
     * No args constructor for use in serialization
     */
    public ProcessInfo() {
    }

    /**
     * @param processes
     * @param memory
     */
    public ProcessInfo(Memory memory, long processId, long threadCount, long processCount, Process[] processes) {
        super();
        this.memory = memory;
        this.processId = processId;
        this.threadCount = threadCount;
        this.processCount = processCount;
        this.processes = processes;
    }

    @JsonProperty("memory")
    public Memory getMemory() {
        return memory;
    }

    @JsonProperty("memory")
    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    @JsonProperty("processId")
    public long getProcessId() {
        return processId;
    }

    @JsonProperty("processId")
    public void setProcessId(long processId) {
        this.processId = processId;
    }

    @JsonProperty("threadCount")
    public long getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(long threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty("processCount")
    public long getProcessCount() {
        return processCount;
    }

    @JsonProperty("processCount")
    public void setProcessCount(long processCount) {
        this.processCount = processCount;
    }

    @JsonProperty("processes")
    public Process[] getProcesses() {
        return processes;
    }

    @JsonProperty("processes")
    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }

}
