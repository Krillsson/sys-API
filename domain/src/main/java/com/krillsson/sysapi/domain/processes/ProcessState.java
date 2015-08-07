package com.krillsson.sysapi.domain.processes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessState {
    public enum State
    {
        UNKOWN,
        SLEEP,
        RUN,
        STOP,
        ZOMBIE,
        IDLE;
    }

    State state;
    String name;
    long ppid;
    int tty;
    int nice;
    int priority;
    long threads;
    int processor;

    public ProcessState(State state, String name, long ppid, int tty, int nice, int priority, long threads, int processor) {
        this.state = state;
        this.name = name;
        this.ppid = ppid;
        this.tty = tty;
        this.nice = nice;
        this.priority = priority;
        this.threads = threads;
        this.processor = processor;
    }

    public ProcessState() {
        this.name = "N/A";
        this.state = State.UNKOWN;
    }

    @JsonProperty
    public State getState() {
        return state;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public long getPpid() {
        return ppid;
    }

    @JsonProperty
    public int getTty() {
        return tty;
    }

    @JsonProperty
    public int getNice() {
        return nice;
    }

    @JsonProperty
    public int getPriority() {
        return priority;
    }

    @JsonProperty
    public long getThreads() {
        return threads;
    }

    @JsonProperty
    public int getProcessor() {
        return processor;
    }
}
