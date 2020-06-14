package com.krillsson.sysapi.core.domain.drives;

public class OsPartition extends Partition {
    private final String volume;
    private final String logicalVolume;
    private final String mount;
    private final String description;
    private final long usableSpace;
    private final long totalSpace;

    public OsPartition(String identification, String name, String type, String uuid, long size, int major, int minor, String mountPoint, String volume, String logicalVolume, String mount, String description, long usableSpace, long totalSpace) {
        super(identification, name, type, uuid, size, major, minor, mountPoint);
        this.volume = volume;
        this.logicalVolume = logicalVolume;
        this.mount = mount;
        this.description = description;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
    }

    public String getVolume() {
        return volume;
    }

    public String getLogicalVolume() {
        return logicalVolume;
    }

    public String getMount() {
        return mount;
    }

    public String getDescription() {
        return description;
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }
}
