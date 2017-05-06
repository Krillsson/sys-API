package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "volume",
        "mount",
        "description",
        "uuid",
        "usableSpace",
        "totalSpace",
        "type"
})
public class OsFileStore {

    @JsonProperty("name")
    private String name;
    @JsonProperty("volume")
    private String volume;
    @JsonProperty("mount")
    private String mount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("usableSpace")
    private long usableSpace;
    @JsonProperty("totalSpace")
    private long totalSpace;
    @JsonProperty("type")
    private String type;

    /**
     * No args constructor for use in serialization
     */
    public OsFileStore() {
    }

    /**
     * @param totalSpace
     * @param usableSpace
     * @param mount
     * @param description
     * @param name
     * @param volume
     * @param uuid
     * @param type
     */
    public OsFileStore(String name, String volume, String mount, String description, String uuid, long usableSpace, long totalSpace, String type) {
        super();
        this.name = name;
        this.volume = volume;
        this.mount = mount;
        this.description = description;
        this.uuid = uuid;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("volume")
    public String getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @JsonProperty("mount")
    public String getMount() {
        return mount;
    }

    @JsonProperty("mount")
    public void setMount(String mount) {
        this.mount = mount;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("uuid")
    public String getuuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setuuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("usableSpace")
    public long getUsableSpace() {
        return usableSpace;
    }

    @JsonProperty("usableSpace")
    public void setUsableSpace(long usableSpace) {
        this.usableSpace = usableSpace;
    }

    @JsonProperty("totalSpace")
    public long getTotalSpace() {
        return totalSpace;
    }

    @JsonProperty("totalSpace")
    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

}
