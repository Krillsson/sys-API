
package com.krillsson.sysapi.dto.motherboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "manufacturer",
    "model",
    "serialNumber",
    "firmware",
    "baseboard"
})
public class ComputerSystem {

    @JsonProperty("manufacturer")
    private String manufacturer;
    @JsonProperty("model")
    private String model;
    @JsonProperty("serialNumber")
    private String serialNumber;
    @JsonProperty("firmware")
    private Firmware firmware;
    @JsonProperty("baseboard")
    private Baseboard baseboard;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ComputerSystem() {
    }

    /**
     * 
     * @param firmware
     * @param model
     * @param baseboard
     * @param manufacturer
     * @param serialNumber
     */
    public ComputerSystem(String manufacturer, String model, String serialNumber, Firmware firmware, Baseboard baseboard) {
        super();
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
        this.firmware = firmware;
        this.baseboard = baseboard;
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

    @JsonProperty("serialNumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    @JsonProperty("serialNumber")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @JsonProperty("firmware")
    public Firmware getFirmware() {
        return firmware;
    }

    @JsonProperty("firmware")
    public void setFirmware(Firmware firmware) {
        this.firmware = firmware;
    }

    @JsonProperty("baseboard")
    public Baseboard getBaseboard() {
        return baseboard;
    }

    @JsonProperty("baseboard")
    public void setBaseboard(Baseboard baseboard) {
        this.baseboard = baseboard;
    }

}
