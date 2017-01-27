package com.krillsson.sysapi.dto.gpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "fanRpm",
        "fanPercent",
        "temperature",
        "coreLoad",
        "memoryLoad"
})
public class GpuHealth {

    @JsonProperty("fanRpm")
    private double fanRpm;
    @JsonProperty("fanPercent")
    private double fanPercent;
    @JsonProperty("temperature")
    private double temperature;
    @JsonProperty("coreLoad")
    private double coreLoad;
    @JsonProperty("memoryLoad")
    private double memoryLoad;

    public GpuHealth() {
    }

    public GpuHealth(double fanRpm, double fanPercent, double temperature, double coreLoad, double memoryLoad) {
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
        this.temperature = temperature;
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
    }

    @JsonProperty("fanRpm")
    public double getFanRpm() {
        return fanRpm;
    }

    @JsonProperty("fanRpm")
    public void setFanRpm(double fanRpm) {
        this.fanRpm = fanRpm;
    }

    @JsonProperty("fanPercent")
    public double getFanPercent() {
        return fanPercent;
    }

    @JsonProperty("fanPercent")
    public void setFanPercent(double fanPercent) {
        this.fanPercent = fanPercent;
    }

    @JsonProperty("temperature")
    public double getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("coreLoad")
    public double getCoreLoad() {
        return coreLoad;
    }

    @JsonProperty("coreLoad")
    public void setCoreLoad(double coreLoad) {
        this.coreLoad = coreLoad;
    }

    @JsonProperty("memoryLoad")
    public double getMemoryLoad() {
        return memoryLoad;
    }

    @JsonProperty("memoryLoad")
    public void setMemoryLoad(double memoryLoad) {
        this.memoryLoad = memoryLoad;
    }

}
