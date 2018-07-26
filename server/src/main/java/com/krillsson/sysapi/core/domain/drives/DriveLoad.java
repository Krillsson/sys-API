package com.krillsson.sysapi.core.domain.drives;

public class DriveLoad {
    private final String name;
    private final DriveValues metrics;
    private final DriveSpeed speed;
    private final DriveHealth health;

    public DriveLoad(String name, DriveValues metrics, DriveSpeed speed, DriveHealth health) {
        this.name = name;
        this.metrics = metrics;
        this.speed = speed;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public DriveValues getMetrics() {
        return metrics;
    }

    public DriveSpeed getSpeed() {
        return speed;
    }

    public DriveHealth getHealth() {
        return health;
    }
}
