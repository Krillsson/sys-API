package com.krillsson.sysapi.domain.filesystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileSystemUsage {
    private long totalSizeKB,
            freeSpaceKB,
            used,
            avail,
            files,
            freeFiles,
            diskReads,
            diskWrites,
            diskReadBytes,
            diskWriteBytes;
    private double diskQueue,
            diskServiceTime,
            usePercent;

    public FileSystemUsage(long totalSizeKB, long freeSpaceKB, long used, long avail, long files, long freeFiles, long diskReads, long diskWrites, long diskReadBytes, long diskWriteBytes, double diskQueue, double diskServiceTime, double usePercent) {
        this.totalSizeKB = totalSizeKB;
        this.freeSpaceKB = freeSpaceKB;
        this.used = used;
        this.avail = avail;
        this.files = files;
        this.freeFiles = freeFiles;
        this.diskReads = diskReads;
        this.diskWrites = diskWrites;
        this.diskReadBytes = diskReadBytes;
        this.diskWriteBytes = diskWriteBytes;
        this.diskQueue = diskQueue;
        this.diskServiceTime = diskServiceTime;
        this.usePercent = usePercent;
    }

    public static FileSystemUsage fromSigarBean(org.hyperic.sigar.FileSystemUsage usage)
    {
        return new FileSystemUsage(usage.getTotal(),
                usage.getFree(),
                usage.getUsed(),
                usage.getAvail(),
                usage.getFiles(),
                usage.getFreeFiles(),
                usage.getDiskReads(),
                usage.getDiskWrites(),
                usage.getDiskReadBytes(),
                usage.getDiskWriteBytes(),
                usage.getDiskQueue(),
                usage.getDiskServiceTime(),
                usage.getUsePercent());
    }

    @JsonProperty
    public long getTotalSizeKB() {
        return totalSizeKB;
    }

    @JsonProperty
    public long getFreeSpaceKB() {
        return freeSpaceKB;
    }

    @JsonProperty
    public long getUsed() {
        return used;
    }

    @JsonProperty
    public long getAvail() {
        return avail;
    }

    @JsonProperty
    public long getFiles() {
        return files;
    }

    @JsonProperty
    public long getFreeFiles() {
        return freeFiles;
    }

    @JsonProperty
    public long getDiskReads() {
        return diskReads;
    }

    @JsonProperty
    public long getDiskWrites() {
        return diskWrites;
    }

    @JsonProperty
    public long getDiskReadBytes() {
        return diskReadBytes;
    }

    @JsonProperty
    public long getDiskWriteBytes() {
        return diskWriteBytes;
    }

    @JsonProperty
    public double getDiskQueue() {
        return diskQueue;
    }

    @JsonProperty
    public double getDiskServiceTime() {
        return diskServiceTime;
    }

    @JsonProperty
    public double getUsePercent() {
        return usePercent;
    }
}
