package com.krillsson.sysapi.dto.storage;

public class StorageInfo {


    private long openFileDescriptors;

    private long maxFileDescriptors;

    private DiskInfo[] diskInfo = null;

    /**
     * No args constructor for use in serialization
     */
    public StorageInfo() {
    }

    /**
     * @param diskInfo
     * @param openFileDescriptors
     * @param maxFileDescriptors
     */
    public StorageInfo(long openFileDescriptors, long maxFileDescriptors, DiskInfo[] diskInfo) {
        super();
        this.openFileDescriptors = openFileDescriptors;
        this.maxFileDescriptors = maxFileDescriptors;
        this.diskInfo = diskInfo;
    }


    public long getOpenFileDescriptors() {
        return openFileDescriptors;
    }


    public void setOpenFileDescriptors(long openFileDescriptors) {
        this.openFileDescriptors = openFileDescriptors;
    }


    public long getMaxFileDescriptors() {
        return maxFileDescriptors;
    }


    public void setMaxFileDescriptors(long maxFileDescriptors) {
        this.maxFileDescriptors = maxFileDescriptors;
    }


    public DiskInfo[] getDiskInfo() {
        return diskInfo;
    }


    public void setDiskInfo(DiskInfo[] diskInfo) {
        this.diskInfo = diskInfo;
    }

}
