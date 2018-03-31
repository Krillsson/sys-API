package com.krillsson.sysapi.dto.drives;

public class DriveSpeed {
    private long readBytesPerSecond;
    private long writeBytesPerSecond;

    public DriveSpeed(long readBytesPerSecond, long writeBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
        this.writeBytesPerSecond = writeBytesPerSecond;
    }

    public DriveSpeed() {
    }

    public long getReadBytesPerSecond() {
        return readBytesPerSecond;
    }

    public long getWriteBytesPerSecond() {
        return writeBytesPerSecond;
    }

    public void setReadBytesPerSecond(long readBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
    }

    public void setWriteBytesPerSecond(long writeBytesPerSecond) {
        this.writeBytesPerSecond = writeBytesPerSecond;
    }
}
