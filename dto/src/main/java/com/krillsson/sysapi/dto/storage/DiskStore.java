
package com.krillsson.sysapi.dto.storage;

import java.util.List;
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
    private Integer size;
    @JsonProperty("reads")
    private Integer reads;
    @JsonProperty("readBytes")
    private Integer readBytes;
    @JsonProperty("writes")
    private Integer writes;
    @JsonProperty("writeBytes")
    private Integer writeBytes;
    @JsonProperty("transferTime")
    private Integer transferTime;
    @JsonProperty("partitions")
    private Partition[] partitions = null;
    @JsonProperty("timeStamp")
    private Integer timeStamp;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DiskStore() {
    }

    /**
     * 
     * @param reads
     * @param model
     * @param timeStamp
     * @param partitions
     * @param name
     * @param writeBytes
     * @param writes
     * @param readBytes
     * @param serial
     * @param transferTime
     * @param size
     */
    public DiskStore(String model, String name, String serial, Integer size, Integer reads, Integer readBytes, Integer writes, Integer writeBytes, Integer transferTime, Partition[] partitions, Integer timeStamp) {
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
        this.timeStamp = timeStamp;
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
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("reads")
    public Integer getReads() {
        return reads;
    }

    @JsonProperty("reads")
    public void setReads(Integer reads) {
        this.reads = reads;
    }

    @JsonProperty("readBytes")
    public Integer getReadBytes() {
        return readBytes;
    }

    @JsonProperty("readBytes")
    public void setReadBytes(Integer readBytes) {
        this.readBytes = readBytes;
    }

    @JsonProperty("writes")
    public Integer getWrites() {
        return writes;
    }

    @JsonProperty("writes")
    public void setWrites(Integer writes) {
        this.writes = writes;
    }

    @JsonProperty("writeBytes")
    public Integer getWriteBytes() {
        return writeBytes;
    }

    @JsonProperty("writeBytes")
    public void setWriteBytes(Integer writeBytes) {
        this.writeBytes = writeBytes;
    }

    @JsonProperty("transferTime")
    public Integer getTransferTime() {
        return transferTime;
    }

    @JsonProperty("transferTime")
    public void setTransferTime(Integer transferTime) {
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

    @JsonProperty("timeStamp")
    public Integer getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("timeStamp")
    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

}
