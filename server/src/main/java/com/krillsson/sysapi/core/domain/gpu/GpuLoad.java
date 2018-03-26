package com.krillsson.sysapi.core.domain.gpu;

public class GpuLoad {
    private final double coreLoad;
    private final double memoryLoad;
    private final GpuHealth health;

    public GpuLoad(double coreLoad, double memoryLoad, GpuHealth health) {
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
        this.health = health;
    }

    public double getCoreLoad() {
        return coreLoad;
    }

    public double getMemoryLoad() {
        return memoryLoad;
    }

    public GpuHealth getHealth() {
        return health;
    }
}
