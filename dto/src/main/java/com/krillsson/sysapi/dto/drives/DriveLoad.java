package com.krillsson.sysapi.dto.drives;

public class DriveLoad {
    private String name;
    private DriveValues metrics;
    private DriveSpeed speed;
    private DriveHealth health;

    public DriveLoad(String name, DriveValues metrics, DriveSpeed speed, DriveHealth health) {
        this.name = name;
        this.metrics = metrics;
        this.speed = speed;
        this.health = health;
    }

    public DriveLoad() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DriveValues getMetrics() {
        return metrics;
    }

    public void setMetrics(DriveValues metrics) {
        this.metrics = metrics;
    }

    public DriveSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(DriveSpeed speed) {
        this.speed = speed;
    }

    public DriveHealth getHealth() {
        return health;
    }

    public void setHealth(DriveHealth health) {
        this.health = health;
    }
}
