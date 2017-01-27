package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "openFileDescriptors",
        "maxFileDescriptors",
        "timeStamp",
        "diskInfo"
})
public class StorageInfo {

    @JsonProperty("openFileDescriptors")
    private Integer openFileDescriptors;
    @JsonProperty("maxFileDescriptors")
    private Integer maxFileDescriptors;
    @JsonProperty("timeStamp")
    private Integer timeStamp;
    @JsonProperty("diskInfo")
    private DiskInfo[] diskInfo = null;

    /**
     * No args constructor for use in serialization
     */
    public StorageInfo() {
    }

    /**
     * @param timeStamp
     * @param diskInfo
     * @param openFileDescriptors
     * @param maxFileDescriptors
     */
    public StorageInfo(Integer openFileDescriptors, Integer maxFileDescriptors, Integer timeStamp, DiskInfo[] diskInfo) {
        super();
        this.openFileDescriptors = openFileDescriptors;
        this.maxFileDescriptors = maxFileDescriptors;
        this.timeStamp = timeStamp;
        this.diskInfo = diskInfo;
    }

    @JsonProperty("openFileDescriptors")
    public Integer getOpenFileDescriptors() {
        return openFileDescriptors;
    }

    @JsonProperty("openFileDescriptors")
    public void setOpenFileDescriptors(Integer openFileDescriptors) {
        this.openFileDescriptors = openFileDescriptors;
    }

    @JsonProperty("maxFileDescriptors")
    public Integer getMaxFileDescriptors() {
        return maxFileDescriptors;
    }

    @JsonProperty("maxFileDescriptors")
    public void setMaxFileDescriptors(Integer maxFileDescriptors) {
        this.maxFileDescriptors = maxFileDescriptors;
    }

    @JsonProperty("timeStamp")
    public Integer getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("timeStamp")
    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    @JsonProperty("diskInfo")
    public DiskInfo[] getDiskInfo() {
        return diskInfo;
    }

    @JsonProperty("diskInfo")
    public void setDiskInfo(DiskInfo[] diskInfo) {
        this.diskInfo = diskInfo;
    }

}
