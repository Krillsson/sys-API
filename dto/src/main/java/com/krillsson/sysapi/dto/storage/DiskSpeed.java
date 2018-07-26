package com.krillsson.sysapi.dto.storage;

public class DiskSpeed {


    private long readBytesPerSecond;


    private long writeBytesPerSecond;

    /**
     * No args constructor for use in serialization
     */
    public DiskSpeed() {
    }

    /**
     * @param readBytesPerSecond
     * @param writeBytesPerSecond
     */
    public DiskSpeed(long readBytesPerSecond, long writeBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
        this.writeBytesPerSecond = writeBytesPerSecond;
    }


    public long getReadBytesPerSecond() {
        return readBytesPerSecond;
    }


    public void setReadBytesPerSecond(long readBytesPerSecond) {
        this.readBytesPerSecond = readBytesPerSecond;
    }


    public long getWriteBytesPerSecond() {
        return writeBytesPerSecond;
    }


    public void setWriteBytesPerSecond(long writeBytesPerSecond) {
        this.writeBytesPerSecond = writeBytesPerSecond;
    }
}
