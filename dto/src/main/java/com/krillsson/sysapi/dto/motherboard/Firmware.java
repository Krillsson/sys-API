package com.krillsson.sysapi.dto.motherboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "manufacturer",
        "name",
        "description",
        "version",
        "releaseDate"
})
public class Firmware {

    @JsonProperty("manufacturer")
    private String manufacturer;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("version")
    private String version;
    @JsonProperty("releaseDate")
    private Date releaseDate = null;

    /**
     * No args constructor for use in serialization
     */
    public Firmware() {
    }

    /**
     * @param releaseDate
     * @param description
     * @param manufacturer
     * @param name
     * @param version
     */
    public Firmware(String manufacturer, String name, String description, String version, Date releaseDate) {
        super();
        this.manufacturer = manufacturer;
        this.name = name;
        this.description = description;
        this.version = version;
        this.releaseDate = releaseDate;
    }

    @JsonProperty("manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @JsonProperty("manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("releaseDate")
    public Date getReleaseDate() {
        return releaseDate;
    }

    @JsonProperty("releaseDate")
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

}
