package com.krillsson.sysapi.core.domain.storage;

public class DiskSpeed {
    private final long readBytesPerSecond;
    private final long writeBytesPerSecond;

    public DiskSpeed(long readBytesPerSecond, long writeBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
        this.writeBytesPerSecond = writeBytesPerSecond;
    }

    public long getReadBytesPerSecond() {
        return readBytesPerSecond;
    }

    public long getWriteBytesPerSecond() {
        return writeBytesPerSecond;
    }
}
