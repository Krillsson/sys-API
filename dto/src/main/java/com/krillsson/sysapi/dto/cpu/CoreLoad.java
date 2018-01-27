package com.krillsson.sysapi.dto.cpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user",
        "nice",
        "sys",
        "idle",
        "ioWait",
        "irq",
        "softIrq",
        "steal"
})
public class CoreLoad {
    @JsonProperty("user")
    private double user;
    @JsonProperty("nice")
    private double nice;
    @JsonProperty("sys")
    private double sys;
    @JsonProperty("idle")
    private double idle;
    @JsonProperty("ioWait")
    private double ioWait;
    @JsonProperty("irq")
    private double irq;
    @JsonProperty("softIrq")
    private double softIrq;
    @JsonProperty("steal")
    private double steal;

    public CoreLoad() {

    }

    public CoreLoad(double user, double nice, double sys, double idle, double ioWait, double irq, double softIrq, double steal) {
        this.user = user;
        this.nice = nice;
        this.sys = sys;
        this.idle = idle;
        this.ioWait = ioWait;
        this.irq = irq;
        this.softIrq = softIrq;
        this.steal = steal;
    }

    @JsonProperty("user")
    public double getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(double user) {
        this.user = user;
    }

    @JsonProperty("nice")
    public double getNice() {
        return nice;
    }

    @JsonProperty("nice")
    public void setNice(double nice) {
        this.nice = nice;
    }

    @JsonProperty("sys")
    public double getSys() {
        return sys;
    }

    @JsonProperty("sys")
    public void setSys(double sys) {
        this.sys = sys;
    }

    @JsonProperty("idle")
    public double getIdle() {
        return idle;
    }

    @JsonProperty("idle")
    public void setIdle(double idle) {
        this.idle = idle;
    }

    @JsonProperty("ioWait")
    public double getioWait() {
        return ioWait;
    }

    @JsonProperty("ioWait")
    public void setioWait(double ioWait) {
        this.ioWait = ioWait;
    }

    @JsonProperty("irq")
    public double getIrq() {
        return irq;
    }

    @JsonProperty("irq")
    public void setIrq(double irq) {
        this.irq = irq;
    }

    @JsonProperty("softIrq")
    public double getsoftIrq() {
        return softIrq;
    }

    @JsonProperty("softIrq")
    public void setsoftIrq(double softIrq) {
        this.softIrq = softIrq;
    }

    @JsonProperty("steal")
    public double getSteal() {
        return steal;
    }

    @JsonProperty("steal")
    public void setSteal(double steal) {
        this.steal = steal;
    }
}
