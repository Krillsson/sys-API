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
    private long processId;
    @JsonProperty("threadCount")
    private long threadCount;
    @JsonProperty("processCount")
    private long processCount;

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
    public OperatingSystem(String manufacturer, String family, Version version, long processId, long threadCount, long processCount) {
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
    public long getProcessId() {
        return processId;
    }

    @JsonProperty("processId")
    public void setProcessId(long processId) {
        this.processId = processId;
    }

    @JsonProperty("threadCount")
    public long getThreadCount() {
        return threadCount;
    }

    @JsonProperty("threadCount")
    public void setThreadCount(long threadCount) {
        this.threadCount = threadCount;
    }

    @JsonProperty("processCount")
    public long getProcessCount() {
        return processCount;
    }

    @JsonProperty("processCount")
    public void setProcessCount(long processCount) {
        this.processCount = processCount;
    }

}
