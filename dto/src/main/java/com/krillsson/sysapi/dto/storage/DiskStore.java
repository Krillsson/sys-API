package com.krillsson.sysapi.dto.storage;

public class DiskStore {


    private String model;

    private String name;

    private String serial;

    private long size;

    private long reads;

    private long readBytes;

    private long writes;

    private long writeBytes;

    private long transferTime;

    private Partition[] partitions = null;

    /**
     * No args constructor for use in serialization
     */
    public DiskStore() {
    }

    /**
     * @param reads
     * @param model
     * @param partitions
     * @param name
     * @param writeBytes
     * @param writes
     * @param readBytes
     * @param serial
     * @param transferTime
     * @param size
     */
    public DiskStore(String model, String name, String serial, long size, long reads, long readBytes, long writes, long writeBytes, long transferTime, Partition[] partitions) {
        super();
        this.model = model;
        this.name = name;
        this.serial = serial;
        this.size = size;
        this.reads = reads;
        this.readBytes = readBytes;
        this.writes = writes;
        this.writeBytes = writeBytes;
        this.transferTime = transferTime;
        this.partitions = partitions;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSerial() {
        return serial;
    }


    public void setSerial(String serial) {
        this.serial = serial;
    }


    public long getSize() {
        return size;
    }


    public void setSize(long size) {
        this.size = size;
    }


    public long getReads() {
        return reads;
    }


    public void setReads(long reads) {
        this.reads = reads;
    }


    public long getReadBytes() {
        return readBytes;
    }


    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }


    public long getWrites() {
        return writes;
    }


    public void setWrites(long writes) {
        this.writes = writes;
    }


    public long getWriteBytes() {
        return writeBytes;
    }


    public void setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
    }


    public long getTransferTime() {
        return transferTime;
    }


    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }


    public Partition[] getPartitions() {
        return partitions;
    }


    public void setPartitions(Partition[] partitions) {
        this.partitions = partitions;
    }

}
