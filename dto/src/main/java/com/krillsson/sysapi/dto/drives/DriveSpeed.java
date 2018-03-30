package com.krillsson.sysapi.dto.drives;

public class DriveSpeed {
    private final long readBytesPerSecond;
    private final long writeBytesPerSecond;

    public DriveSpeed(long readBytesPerSecond, long writeBytesPerSecond) {
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
