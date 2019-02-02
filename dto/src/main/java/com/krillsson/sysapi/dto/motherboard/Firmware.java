package com.krillsson.sysapi.dto.motherboard;

public class Firmware {


    private String manufacturer;

    private String name;

    private String description;

    private String version;

    private String releaseDate = null;

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
    public Firmware(String manufacturer, String name, String description, String version, String releaseDate) {
        super();
        this.manufacturer = manufacturer;
        this.name = name;
        this.description = description;
        this.version = version;
        this.releaseDate = releaseDate;
    }


    public String getManufacturer() {
        return manufacturer;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getReleaseDate() {
        return releaseDate;
    }


    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
