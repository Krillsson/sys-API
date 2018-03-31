package com.krillsson.sysapi.dto.processes;

public class ProcessInfo {


    private Memory memory;

    private Process[] processes = null;

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
    public ProcessInfo(Memory memory, long processId, long threadCount, long processCount, Process[] processes) {
        super();
        this.memory = memory;
        this.processId = processId;
        this.threadCount = threadCount;
        this.processCount = processCount;
        this.processes = processes;
    }


    public Memory getMemory() {
        return memory;
    }


    public void setMemory(Memory memory) {
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


    public Process[] getProcesses() {
        return processes;
    }


    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }

}
