package se.christianjensen.maintenance.representation.processes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Process {
    private long pid;
    private String[] args;
    private ProcessExecutable executable;
    private ProcessCreator creator;
    private ProcessCpu cpu;
    private ProcessMemory memory;

    public Process(long pid, String[] args, ProcessExecutable executable, ProcessCreator creator, ProcessCpu cpu, ProcessMemory memory) {
        this.pid = pid;
        this.args = args;
        this.executable = executable;
        this.creator = creator;
        this.cpu = cpu;
        this.memory = memory;
    }

    @JsonProperty
    public long getPid() {
        return pid;
    }

    @JsonProperty
    public String[] getArgs() {
        return args;
    }

    @JsonProperty
    public ProcessExecutable getExecutable() {
        return executable;
    }

    @JsonProperty
    public ProcessCreator getCreator() {
        return creator;
    }

    @JsonProperty
    public ProcessCpu getCpu() {
        return cpu;
    }

    @JsonProperty
    public ProcessMemory getMemory() {
        return memory;
    }
}
