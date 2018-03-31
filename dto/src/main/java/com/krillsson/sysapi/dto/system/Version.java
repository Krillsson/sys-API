package com.krillsson.sysapi.dto.system;

public class Version {


    private String version;

    private String codeName;

    private String buildNumber;

    /**
     * No args constructor for use in serialization
     */
    public Version() {
    }

    /**
     * @param codeName
     * @param buildNumber
     * @param version
     */
    public Version(String version, String codeName, String buildNumber) {
        super();
        this.version = version;
        this.codeName = codeName;
        this.buildNumber = buildNumber;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getCodeName() {
        return codeName;
    }


    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }


    public String getBuildNumber() {
        return buildNumber;
    }


    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

}
