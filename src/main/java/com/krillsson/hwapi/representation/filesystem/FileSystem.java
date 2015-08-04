package com.krillsson.hwapi.representation.filesystem;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* Created by christian on 2014-11-30.
*/
public final class FileSystem {
    private final String deviceName;
    private final String mountPoint;
    private final FSType genericFSType;
    private final String osSpecificFSType;
    private final long totalSizeKB;
    private final long freeSpaceKB;

    public FileSystem( //
                       String deviceName, String mountPoint, //
                       FSType genericFSType, String osSpecificFSType, //
                       long totalSizeKB, long freeSpaceKB) {
        this.deviceName = deviceName;
        this.mountPoint = mountPoint;
        this.genericFSType = genericFSType;
        this.osSpecificFSType = osSpecificFSType;
        this.totalSizeKB = totalSizeKB;
        this.freeSpaceKB = freeSpaceKB;
    }

    public static FileSystem fromSigarBean(org.hyperic.sigar.FileSystem fs,
            long totalSizeKB, long freeSpaceKB) {
        return new FileSystem( //
                fs.getDevName(), fs.getDirName(), //
                FSType.values()[fs.getType()], fs.getSysTypeName(), //
                totalSizeKB, freeSpaceKB);
    }
    @JsonProperty
    public String deviceName() { return deviceName; }
    @JsonProperty
    public String mountPoint() { return mountPoint; }
    @JsonProperty
    public FSType genericFSType() { return genericFSType; }
    @JsonProperty
    public String osSpecificFSType() { return osSpecificFSType; }
    @JsonProperty
    public long totalSizeKB() { return totalSizeKB; }
    @JsonProperty
    public long freeSpaceKB() { return freeSpaceKB; }

}
