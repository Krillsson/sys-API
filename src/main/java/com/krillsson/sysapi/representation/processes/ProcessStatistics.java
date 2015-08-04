package com.krillsson.sysapi.representation.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.ProcStat;

public class ProcessStatistics {
    long total,
    idle,
    running,
    sleeping,
    stopped,
    zombie,
    threads;

    public ProcessStatistics(long total,
                             long idle,
                             long running,
                             long sleeping,
                             long stopped,
                             long zombie,
                             long threads) {
        this.total = total;
        this.idle = idle;
        this.running = running;
        this.sleeping = sleeping;
        this.stopped = stopped;
        this.zombie = zombie;
        this.threads = threads;
    }

    public ProcessStatistics() {

    }

    public static ProcessStatistics fromSigarBean(ProcStat procStat) {
        return new ProcessStatistics(procStat.getTotal(),
                procStat.getIdle(),
                procStat.getRunning(),
                procStat.getSleeping(),
                procStat.getStopped(),
                procStat.getZombie(),
                procStat.getThreads());
    }

    @JsonProperty
    public long getTotal() {
        return total;
    }

    @JsonProperty
    public long getIdle() {
        return idle;
    }

    @JsonProperty
    public long getRunning() {
        return running;
    }

    @JsonProperty
    public long getSleeping() {
        return sleeping;
    }

    @JsonProperty
    public long getStopped() {
        return stopped;
    }

    @JsonProperty
    public long getZombie() {
        return zombie;
    }

    @JsonProperty
    public long getThreads() {
        return threads;
    }
}
