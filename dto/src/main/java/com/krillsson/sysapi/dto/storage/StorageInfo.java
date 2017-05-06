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
    private long openFileDescriptors;
    @JsonProperty("maxFileDescriptors")
    private long maxFileDescriptors;
    @JsonProperty("diskInfo")
    private DiskInfo[] diskInfo = null;

    /**
     * No args constructor for use in serialization
     */
    public StorageInfo() {
    }

    /**
     * @param diskInfo
     * @param openFileDescriptors
     * @param maxFileDescriptors
     */
    public StorageInfo(long openFileDescriptors, long maxFileDescriptors, DiskInfo[] diskInfo) {
        super();
        this.openFileDescriptors = openFileDescriptors;
        this.maxFileDescriptors = maxFileDescriptors;
        this.diskInfo = diskInfo;
    }

    @JsonProperty("openFileDescriptors")
    public long getOpenFileDescriptors() {
        return openFileDescriptors;
    }

    @JsonProperty("openFileDescriptors")
    public void setOpenFileDescriptors(long openFileDescriptors) {
        this.openFileDescriptors = openFileDescriptors;
    }

    @JsonProperty("maxFileDescriptors")
    public long getMaxFileDescriptors() {
        return maxFileDescriptors;
    }

    @JsonProperty("maxFileDescriptors")
    public void setMaxFileDescriptors(long maxFileDescriptors) {
        this.maxFileDescriptors = maxFileDescriptors;
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
