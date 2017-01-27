package com.krillsson.sysapi.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "version",
        "codeName",
        "buildNumber"
})
public class Version {

    @JsonProperty("version")
    private String version;
    @JsonProperty("codeName")
    private String codeName;
    @JsonProperty("buildNumber")
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

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("codeName")
    public String getCodeName() {
        return codeName;
    }

    @JsonProperty("codeName")
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    @JsonProperty("buildNumber")
    public String getBuildNumber() {
        return buildNumber;
    }

    @JsonProperty("buildNumber")
    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

}
