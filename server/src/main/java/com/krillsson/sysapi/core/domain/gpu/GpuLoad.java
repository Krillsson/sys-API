package com.krillsson.sysapi.core.domain.gpu;

public class GpuLoad {
    private final String name;
    private final double coreLoad;
    private final double memoryLoad;
    private final GpuHealth health;

    public GpuLoad(String name, double coreLoad, double memoryLoad, GpuHealth health) {
        this.name = name;
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
        this.health = health;
    }

    public String getName() {
        return name;
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
