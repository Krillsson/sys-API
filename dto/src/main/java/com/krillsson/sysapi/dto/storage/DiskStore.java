package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "model",
        "name",
        "serial",
        "size",
        "reads",
        "readBytes",
        "writes",
        "writeBytes",
        "transferTime",
        "partitions",
        "timeStamp"
})
public class DiskStore {

    @JsonProperty("model")
    private String model;
    @JsonProperty("name")
    private String name;
    @JsonProperty("serial")
    private String serial;
    @JsonProperty("size")
    private long size;
    @JsonProperty("reads")
    private long reads;
    @JsonProperty("readBytes")
    private long readBytes;
    @JsonProperty("writes")
    private long writes;
    @JsonProperty("writeBytes")
    private long writeBytes;
    @JsonProperty("transferTime")
    private long transferTime;
    @JsonProperty("partitions")
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

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("serial")
    public String getSerial() {
        return serial;
    }

    @JsonProperty("serial")
    public void setSerial(String serial) {
        this.serial = serial;
    }

    @JsonProperty("size")
    public long getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(long size) {
        this.size = size;
    }

    @JsonProperty("reads")
    public long getReads() {
        return reads;
    }

    @JsonProperty("reads")
    public void setReads(long reads) {
        this.reads = reads;
    }

    @JsonProperty("readBytes")
    public long getReadBytes() {
        return readBytes;
    }

    @JsonProperty("readBytes")
    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }

    @JsonProperty("writes")
    public long getWrites() {
        return writes;
    }

    @JsonProperty("writes")
    public void setWrites(long writes) {
        this.writes = writes;
    }

    @JsonProperty("writeBytes")
    public long getWriteBytes() {
        return writeBytes;
    }

    @JsonProperty("writeBytes")
    public void setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
    }

    @JsonProperty("transferTime")
    public long getTransferTime() {
        return transferTime;
    }

    @JsonProperty("transferTime")
    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    @JsonProperty("partitions")
    public Partition[] getPartitions() {
        return partitions;
    }

    @JsonProperty("partitions")
    public void setPartitions(Partition[] partitions) {
        this.partitions = partitions;
    }

}
