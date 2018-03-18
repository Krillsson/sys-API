package com.krillsson.sysapi.core.domain.storage;

public class DiskLoad {
    private final String name;
    private final DiskMetrics metrics;
    private final DiskSpeed speed;
    private final DiskHealth health;

    public DiskLoad(String name, DiskMetrics metrics, DiskSpeed speed, DiskHealth health) {
        this.name = name;
        this.metrics = metrics;
        this.speed = speed;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public DiskMetrics getMetrics() {
        return metrics;
    }

    public DiskSpeed getSpeed() {
        return speed;
    }

    public DiskHealth getHealth() {
        return health;
    }
}
