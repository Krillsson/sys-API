package com.krillsson.sysapi.core.domain.cpu;

public class CoreLoad {

    private final double user;
    private final double nice;
    private final double sys;
    private final double idle;
    private final double ioWait;
    private final double irq;
    private final double softIrq;
    private final double steal;

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
}
