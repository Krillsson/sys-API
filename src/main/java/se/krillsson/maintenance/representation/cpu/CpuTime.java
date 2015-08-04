package se.krillsson.maintenance.representation.cpu;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.CpuPerc;

/**
* Created by christian on 2014-11-30.
*/
public final class CpuTime {

    private final double user;
    private final double sys;
    private final double nice;
    private final double waiting;
    private final double idle;
    private final double irq;

    public CpuTime( //
                    double user, double sys, //
                    double nice, double waiting, //
                    double idle, double irq) {
        this.user = user;
        this.sys = sys;
        this.nice = nice;
        this.waiting = waiting;
        this.idle = idle;
        this.irq = irq;
    }

    public static CpuTime fromSigarBean(CpuPerc cp) {
        return new CpuTime( //
                cp.getUser(), cp.getSys(), //
                cp.getNice(), cp.getWait(), //
                cp.getIdle(), cp.getIrq());
    }
    @JsonProperty
    public double user() { return user; }
    @JsonProperty
    public double sys() { return sys; }
    @JsonProperty
    public double nice() { return nice; }
    @JsonProperty
    public double waiting() { return waiting; }
    @JsonProperty
    public double idle() { return idle; }
    @JsonProperty
    public double irq() { return irq; }
}
