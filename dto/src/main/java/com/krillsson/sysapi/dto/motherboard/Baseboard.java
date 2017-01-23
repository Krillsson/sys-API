
package com.krillsson.sysapi.dto.motherboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "manufacturer",
    "model",
    "version",
    "serialNumber"
})
public class Baseboard {

    @JsonProperty("manufacturer")
    private String manufacturer;
    @JsonProperty("model")
    private String model;
    @JsonProperty("version")
    private String version;
    @JsonProperty("serialNumber")
    private String serialNumber;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Baseboard() {
    }

    /**
     * 
     * @param model
     * @param manufacturer
     * @param serialNumber
     * @param version
     */
    public Baseboard(String manufacturer, String model, String version, String serialNumber) {
        super();
        this.manufacturer = manufacturer;
        this.model = model;
        this.version = version;
        this.serialNumber = serialNumber;
    }

    @JsonProperty("manufacturer")
    public String getManufacturer() {
        return manufacturer;
    }

    @JsonProperty("manufacturer")
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("serialNumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    @JsonProperty("serialNumber")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
