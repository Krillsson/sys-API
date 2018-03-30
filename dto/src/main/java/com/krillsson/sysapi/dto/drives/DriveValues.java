package com.krillsson.sysapi.dto.drives;

public class DriveValues {
    private final long usableSpace;
    private final long totalSpace;
    private final long openFileDescriptors;
    private final long maxFileDescriptors;
    private final long reads;
    private final long readBytes;
    private final long writes;
    private final long writeBytes;

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
}
