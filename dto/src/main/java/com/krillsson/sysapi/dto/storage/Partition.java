package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identification",
        "name",
        "type",
        "uuid",
        "size",
        "major",
        "minor",
        "mountPoint"
})
public class Partition {

    @JsonProperty("identification")
    private String identification;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("major")
    private Integer major;
    @JsonProperty("minor")
    private Integer minor;
    @JsonProperty("mountPoint")
    private String mountPoint;

    /**
     * No args constructor for use in serialization
     */
    public Partition() {
    }

    /**
     * @param minor
     * @param identification
     * @param name
     * @param mountPoint
     * @param uuid
     * @param type
     * @param major
     * @param size
     */
    public Partition(String identification, String name, String type, String uuid, Integer size, Integer major, Integer minor, String mountPoint) {
        super();
        this.identification = identification;
        this.name = name;
        this.type = type;
        this.uuid = uuid;
        this.size = size;
        this.major = major;
        this.minor = minor;
        this.mountPoint = mountPoint;
    }

    @JsonProperty("identification")
    public String getIdentification() {
        return identification;
    }

    @JsonProperty("identification")
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("major")
    public Integer getMajor() {
        return major;
    }

    @JsonProperty("major")
    public void setMajor(Integer major) {
        this.major = major;
    }

    @JsonProperty("minor")
    public Integer getMinor() {
        return minor;
    }

    @JsonProperty("minor")
    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    @JsonProperty("mountPoint")
    public String getMountPoint() {
        return mountPoint;
    }

    @JsonProperty("mountPoint")
    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

}
