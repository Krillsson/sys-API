package com.krillsson.sysapi.dto.motherboard;

public class Baseboard {


    private String manufacturer;

    private String model;

    private String version;

    private String serialNumber;

    /**
     * No args constructor for use in serialization
     */
    public Baseboard() {
    }

    /**
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


    public String getManufacturer() {
        return manufacturer;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
