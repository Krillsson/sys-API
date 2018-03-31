package com.krillsson.sysapi.dto.cpu;

public class CoreLoad {

    private double user;
    private double nice;
    private double sys;
    private double idle;
    private double ioWait;
    private double irq;
    private double softIrq;
    private double steal;

    public CoreLoad(double user, double nice, double sys, double idle, double ioWait, double irq, double softIrq, double steal)
    {
        this.user = user;
        this.nice = nice;
        this.sys = sys;
        this.idle = idle;
        this.ioWait = ioWait;
        this.irq = irq;
        this.softIrq = softIrq;
        this.steal = steal;
    }

    public CoreLoad() {
    }

    public double getUser()
    {
        return user;
    }

    public double getNice()
    {
        return nice;
    }

    public double getSys()
    {
        return sys;
    }

    public double getIdle()
    {
        return idle;
    }

    public double getIoWait()
    {
        return ioWait;
    }

    public double getIrq()
    {
        return irq;
    }

    public double getSoftIrq()
    {
        return softIrq;
    }

    public double getSteal() {
        return steal;
    }

    public void setUser(double user) {
        this.user = user;
    }

    public void setNice(double nice) {
        this.nice = nice;
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public void setIdle(double idle) {
        this.idle = idle;
    }

    public void setIoWait(double ioWait) {
        this.ioWait = ioWait;
    }

    public void setIrq(double irq) {
        this.irq = irq;
    }

    public void setSoftIrq(double softIrq) {
        this.softIrq = softIrq;
    }

    public void setSteal(double steal) {
        this.steal = steal;
    }
}
