package com.krillsson.sysapi.dto.processes;

import com.krillsson.sysapi.dto.memory.MemoryLoad;

import java.util.List;

public class ProcessInfo {


    private MemoryLoad memory;

    private List<Process> processes = null;

    private long processId;

    private long threadCount;

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
    public ProcessInfo(MemoryLoad memory, long processId, long threadCount, long processCount, List<Process> processes) {
        super();
        this.memory = memory;
        this.processId = processId;
        this.threadCount = threadCount;
        this.processCount = processCount;
        this.processes = processes;
    }


    public MemoryLoad getMemory() {
        return memory;
    }


    public void setMemory(MemoryLoad memory) {
        this.memory = memory;
    }


    public long getProcessId() {
        return processId;
    }


    public void setProcessId(long processId) {
        this.processId = processId;
    }


    public long getThreadCount() {
        return threadCount;
    }


    public void setThreadCount(long threadCount) {
        this.threadCount = threadCount;
    }


    public long getProcessCount() {
        return processCount;
    }


    public void setProcessCount(long processCount) {
        this.processCount = processCount;
    }


    public List<Process> getProcesses() {
        return processes;
    }


    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

}
