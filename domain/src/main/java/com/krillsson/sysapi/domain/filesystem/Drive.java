package com.krillsson.sysapi.domain.filesystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Drive
{
    private final String deviceName;
    private final String mountPoint;
    private final FSType genericFSType;
    private final String osSpecificFSType;
    private final FileSystemUsage usage;

    public Drive(String deviceName, String mountPoint,
                 FSType genericFSType, String osSpecificFSType,
                 FileSystemUsage usage) {
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
    public FSType genericFSType() {
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
}
