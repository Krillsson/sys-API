package com.krillsson.sysapi.domain.filesystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Drive
{
    private final String deviceName;
    private final String mountPoint;
    private final FileSystemType genericFSType;
    private final String osSpecificFSType;
    private final FileSystemUsage usage;
    private DriveHealth health;

    public Drive(String deviceName, String mountPoint,
                 FileSystemType genericFSType, String osSpecificFSType,
                 FileSystemUsage usage, DriveHealth health) {
        this.deviceName = deviceName;
        this.mountPoint = mountPoint;
        this.genericFSType = genericFSType;
        this.osSpecificFSType = osSpecificFSType;
        this.usage = usage;
        this.health = health;
    }

    public Drive(String deviceName, String mountPoint, FileSystemType genericFSType, String osSpecificFSType, FileSystemUsage usage)
    {
        this.deviceName = deviceName;
        this.mountPoint = mountPoint;
        this.genericFSType = genericFSType;
        this.osSpecificFSType = osSpecificFSType;
        this.usage = usage;
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

    public void setHealth(DriveHealth health)
    {
        this.health = health;
    }
}
