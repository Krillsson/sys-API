package com.krillsson.sysapi.dto.gpu;

public class GpuLoad {
    private double coreLoad;
    private double memoryLoad;
    private GpuHealth health;

    public GpuLoad(double coreLoad, double memoryLoad, GpuHealth health) {
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
        this.health = health;
    }

    public GpuLoad() {
    }

    public double getCoreLoad() {
        return coreLoad;
    }

    public void setCoreLoad(double coreLoad) {
        this.coreLoad = coreLoad;
    }

    public double getMemoryLoad() {
        return memoryLoad;
    }

    public void setMemoryLoad(double memoryLoad) {
        this.memoryLoad = memoryLoad;
    }

    public GpuHealth getHealth() {
        return health;
    }

    public void setHealth(GpuHealth health) {
        this.health = health;
    }
}
