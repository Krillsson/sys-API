package com.krillsson.sysapi.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "manufacturer",
        "platform",
        "family",
        "version",
        "processId",
        "threadCount",
        "processCount"
})
public class OperatingSystem {

    @JsonProperty("manufacturer")
    private String manufacturer;
    @JsonProperty("family")
    private String family;
    @JsonProperty("version")
    private Version version;

    /**
     * No args constructor for use in serialization
     */
    public OperatingSystem() {
    }

    /**
     * @param family
     * @param manufacturer
     * @param version
     */
    public OperatingSystem(String manufacturer, String family, Version version) {
        this.manufacturer = manufacturer;
        this.family = family;
        this.version = version;
    }

    @JsonProperty("manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @JsonProperty("manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @JsonProperty("family")
    public String getFamily() {
        return family;
    }

    @JsonProperty("family")
    public void setFamily(String family) {
        this.family = family;
    }

    @JsonProperty("version")
    public Version getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Version version) {
        this.version = version;
    }
}
