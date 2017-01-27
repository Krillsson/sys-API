package com.krillsson.sysapi.dto.processes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "memory",
        "processes"
})
public class ProcessInfo {

    @JsonProperty("memory")
    private Memory memory;
    @JsonProperty("processes")
    private Process[] processes = null;

    /**
     * No args constructor for use in serialization
     */
    public ProcessInfo() {
    }

    /**
     * @param processes
     * @param memory
     */
    public ProcessInfo(Memory memory, Process[] processes) {
        super();
        this.memory = memory;
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

    @JsonProperty("processes")
    public Process[] getProcesses() {
        return processes;
    }

    @JsonProperty("processes")
    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }

}
