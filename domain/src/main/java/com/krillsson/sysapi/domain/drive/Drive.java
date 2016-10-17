package com.krillsson.sysapi.domain.drive;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Drive
{
    private String deviceName;
    private final int id;
    private final String mountPoint;
    private final FileSystemType genericFSType;
    private final String osSpecificFSType;
    private final FileSystemUsage usage;
    private DriveHealth health;
    private DriveLoad load;

    public Drive(String deviceName, int id, String mountPoint,
                 FileSystemType genericFSType, String osSpecificFSType,
                 FileSystemUsage usage, DriveHealth health, DriveLoad load) {
        this.deviceName = deviceName;
        this.id = id;
        this.mountPoint = mountPoint;
        this.genericFSType = genericFSType;
        this.osSpecificFSType = osSpecificFSType;
        this.usage = usage;
        this.health = health;
        this.load = load;
    }

    public Drive(String deviceName, int id, String mountPoint, FileSystemType genericFSType, String osSpecificFSType, FileSystemUsage usage)
    {
        this.deviceName = deviceName;
        this.id = id;
        this.mountPoint = mountPoint;
        this.genericFSType = genericFSType;
        this.osSpecificFSType = osSpecificFSType;
        this.usage = usage;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String deviceName() {
        return deviceName;
    }

    @JsonProperty
    public String mountPoint() {
        return mountPoint;
    }

    @JsonProperty
    public FileSystemType genericFSType() {
        return genericFSType;
    }

    @JsonProperty
    public String osSpecificFSType() {
        return osSpecificFSType;
    }

    @JsonProperty
    public FileSystemUsage getUsage() {
        return usage;
    }

    @JsonProperty
    public DriveHealth getHealth()
    {
        return health;
    }

    @JsonProperty
    public DriveLoad getLoad() {
        return load;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setHealth(DriveHealth health)
    {
        this.health = health;
    }

    public void setLoad(DriveLoad load) {
        this.load = load;
    }
}
