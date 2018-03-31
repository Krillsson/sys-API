package com.krillsson.sysapi.dto.drives;

public class Partition {
    private String identification;
    private String name;
    private String type;
    private String uuid;
    private long size;
    private int major;
    private int minor;
    private String mountPoint;

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

    public Partition() {
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

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }
}
