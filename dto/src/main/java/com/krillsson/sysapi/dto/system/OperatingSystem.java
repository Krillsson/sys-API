package com.krillsson.sysapi.dto.system;

public class OperatingSystem {


    private String manufacturer;

    private String family;

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


    public String getManufacturer() {
        return manufacturer;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getFamily() {
        return family;
    }


    public void setFamily(String family) {
        this.family = family;
    }


    public Version getVersion() {
        return version;
    }


    public void setVersion(Version version) {
        this.version = version;
    }
}
