
package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "diskStore",
    "diskHealth",
    "osFileStore"
})
public class DiskInfo {

    @JsonProperty("diskStore")
    private DiskStore diskStore;
    @JsonProperty("diskHealth")
    private DiskHealth diskHealth;
    @JsonProperty("osFileStore")
    private OsFileStore osFileStore;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DiskInfo() {
    }

    /**
     * 
     * @param diskStore
     * @param osFileStore
     * @param diskHealth
     */
    public DiskInfo(DiskStore diskStore, DiskHealth diskHealth, OsFileStore osFileStore) {
        super();
        this.diskStore = diskStore;
        this.diskHealth = diskHealth;
        this.osFileStore = osFileStore;
    }

    @JsonProperty("diskStore")
    public DiskStore getDiskStore() {
        return diskStore;
    }

    @JsonProperty("diskStore")
    public void setDiskStore(DiskStore diskStore) {
        this.diskStore = diskStore;
    }

    @JsonProperty("diskHealth")
    public DiskHealth getDiskHealth() {
        return diskHealth;
    }

    @JsonProperty("diskHealth")
    public void setDiskHealth(DiskHealth diskHealth) {
        this.diskHealth = diskHealth;
    }

    @JsonProperty("osFileStore")
    public OsFileStore getOsFileStore() {
        return osFileStore;
    }

    @JsonProperty("osFileStore")
    public void setOsFileStore(OsFileStore osFileStore) {
        this.osFileStore = osFileStore;
    }

}
