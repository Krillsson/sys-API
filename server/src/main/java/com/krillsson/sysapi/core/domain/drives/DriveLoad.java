package com.krillsson.sysapi.core.domain.drives;

public class DriveLoad {
    private final String name;
    private final String serial;
    private final DriveValues values;
    private final DriveSpeed speed;
    private final DriveHealth health;

    public DriveLoad(String name, String serial, DriveValues values, DriveSpeed speed, DriveHealth health) {
        this.name = name;
        this.serial = serial;
        this.values = values;
        this.speed = speed;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public String getSerial() {
        return serial;
    }

    public DriveValues getValues() {
        return values;
    }

    public DriveSpeed getSpeed() {
        return speed;
    }

    public DriveHealth getHealth() {
        return health;
    }
}
