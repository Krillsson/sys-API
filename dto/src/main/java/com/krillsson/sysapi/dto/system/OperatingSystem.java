package com.krillsson.sysapi.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "manufacturer",
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
    @JsonProperty("processId")
    private Integer processId;
    @JsonProperty("threadCount")
    private Integer threadCount;
    @JsonProperty("processCount")
    private Integer processCount;

    /**
     * No args constructor for use in serialization
     */
    public OperatingSystem() {
    }

    /**
     * @param processId
     * @param family
     * @param manufacturer
     * @param threadCount
     * @param processCount
     * @param version
     */
    public OperatingSystem(String manufacturer, String family, Version version, Integer processId, Integer threadCount, Integer processCount) {
        super();
        this.manufacturer = manufacturer;
        this.family = family;
        this.version = version;
        this.processId = processId;
        this.threadCount = threadCount;
        this.processCount = processCount;
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

    @JsonProperty("processId")
    public Integer getProcessId() {
        return processId;
    }

    @JsonProperty("processId")
    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    @JsonProperty("threadCount")
    public Integer getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty("processCount")
    public Integer getProcessCount() {
        return processCount;
    }

    @JsonProperty("processCount")
    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }

}
