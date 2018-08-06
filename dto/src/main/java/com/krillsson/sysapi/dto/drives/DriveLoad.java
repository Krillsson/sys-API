package com.krillsson.sysapi.dto.drives;

public class DriveLoad {
    private String name;
    private String serial;
    private DriveValues values;
    private DriveSpeed speed;
    private DriveHealth health;

    public DriveLoad(String name, String serial, DriveValues values, DriveSpeed speed, DriveHealth health) {
        this.name = name;
        this.serial = serial;
        this.values = values;
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public DriveValues getValues() {
        return values;
    }

    public void setValues(DriveValues values) {
        this.values = values;
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
