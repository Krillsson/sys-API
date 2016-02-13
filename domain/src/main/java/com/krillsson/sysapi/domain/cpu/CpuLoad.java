package com.krillsson.sysapi.domain.cpu;

import com.fasterxml.jackson.annotation.JsonProperty;
public final class CpuLoad
{
    private double temperature;
    private final double user;
    private final double sys;
    private final double nice;
    private final double waiting;
    private final double idle;
    private final double irq;

    public CpuLoad( //
                    double temperature, double user, double sys, //
                    double nice, double waiting, //
                    double idle, double irq) {
        this.temperature = temperature;
        this.user = user;
        this.sys = sys;
        this.nice = nice;
        this.waiting = waiting;
        this.idle = idle;
        this.irq = irq;
    }

    public CpuLoad(double user, double sys, double nice, double waiting, double idle, double irq)
    {
        this.user = user;
        this.sys = sys;
        this.nice = nice;
        this.waiting = waiting;
        this.idle = idle;
        this.irq = irq;
    }

    @JsonProperty
    public double getTemperature() {return temperature;}
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

    public void setTemperature(double temperature)
    {
        this.temperature = temperature;
    }
}
