package com.krillsson.maintenance.representation.processes;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.ProcCpu;

public class ProcessCpu {
    double percent;
    long lastTime, startTime, user, sys, total;

    public ProcessCpu(double percent, long lastTime, long startTime, long user, long sys, long total) {
        this.percent = percent;
        this.lastTime = lastTime;
        this.startTime = startTime;
        this.user = user;
        this.sys = sys;
        this.total = total;
    }

    public ProcessCpu() {

    }

    public static ProcessCpu fromSigarBean(ProcCpu procCpu){
        return new ProcessCpu(procCpu.getPercent(), procCpu.getLastTime(), procCpu.getStartTime(), procCpu.getUser(), procCpu.getSys(), procCpu.getTotal());
    }

    @JsonProperty
    public double getPercent() {
        return percent;
    }

    @JsonProperty
    public long getLastTime() {
        return lastTime;
    }

    @JsonProperty
    public long getStartTime() {
        return startTime;
    }

    @JsonProperty
    public long getUser() {
        return user;
    }

    @JsonProperty
    public long getSys() {
        return sys;
    }

    @JsonProperty
    public long getTotal() {
        return total;
    }
}
