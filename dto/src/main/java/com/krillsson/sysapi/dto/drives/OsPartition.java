package com.krillsson.sysapi.dto.drives;

public class OsPartition extends Partition {
    private String volume;
    private String logicalVolume;
    private String mount;
    private String description;
    private long usableSpace;
    private long totalSpace;

    public OsPartition(String identification, String name, String type, String uuid, long size, int major, int minor, String mountPoint, String volume, String logicalVolume, String mount, String description, long usableSpace, long totalSpace) {
        super(identification, name, type, uuid, size, major, minor, mountPoint);
        this.volume = volume;
        this.logicalVolume = logicalVolume;
        this.mount = mount;
        this.description = description;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
    }

    public OsPartition() {
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getLogicalVolume() {
        return logicalVolume;
    }

    public void setLogicalVolume(String logicalVolume) {
        this.logicalVolume = logicalVolume;
    }

    public String getMount() {
        return mount;
    }

    public void setMount(String mount) {
        this.mount = mount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public void setUsableSpace(long usableSpace) {
        this.usableSpace = usableSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }
}
