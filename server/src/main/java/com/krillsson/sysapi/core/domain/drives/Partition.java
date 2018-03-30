package com.krillsson.sysapi.core.domain.drives;

public class Partition {
    private final String identification;
    private final String name;
    private final String type;
    private final String uuid;
    private final long size;
    private final int major;
    private final int minor;
    private final String mountPoint;

    public Partition(String identification, String name, String type, String uuid, long size, int major, int minor, String mountPoint) {
        this.identification = identification;
        this.name = name;
        this.type = type;
        this.uuid = uuid;
        this.size = size;
        this.major = major;
        this.minor = minor;
        this.mountPoint = mountPoint;
    }

    public String getIdentification() {
        return identification;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public long getSize() {
        return size;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getMountPoint() {
        return mountPoint;
    }
}
