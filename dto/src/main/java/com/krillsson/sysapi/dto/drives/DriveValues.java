package com.krillsson.sysapi.dto.drives;

public class DriveValues {
    private long usableSpace;
    private long totalSpace;
    private long openFileDescriptors;
    private long maxFileDescriptors;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;

    public DriveValues(long usableSpace, long totalSpace, long openFileDescriptors, long maxFileDescriptors, long reads, long readBytes, long writes, long writeBytes) {
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
        this.openFileDescriptors = openFileDescriptors;
        this.maxFileDescriptors = maxFileDescriptors;
        this.reads = reads;
        this.readBytes = readBytes;
        this.writes = writes;
        this.writeBytes = writeBytes;
    }

    public DriveValues() {
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getOpenFileDescriptors() {
        return openFileDescriptors;
    }

    public long getMaxFileDescriptors() {
        return maxFileDescriptors;
    }

    public long getReads() {
        return reads;
    }

    public long getReadBytes() {
        return readBytes;
    }

    public long getWrites() {
        return writes;
    }

    public long getWriteBytes() {
        return writeBytes;
    }

    public void setUsableSpace(long usableSpace) {
        this.usableSpace = usableSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public void setOpenFileDescriptors(long openFileDescriptors) {
        this.openFileDescriptors = openFileDescriptors;
    }

    public void setMaxFileDescriptors(long maxFileDescriptors) {
        this.maxFileDescriptors = maxFileDescriptors;
    }

    public void setReads(long reads) {
        this.reads = reads;
    }

    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }

    public void setWrites(long writes) {
        this.writes = writes;
    }

    public void setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
    }
}
