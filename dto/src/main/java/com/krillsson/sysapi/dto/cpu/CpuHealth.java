package com.krillsson.sysapi.dto.cpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "temperatures",
        "voltage",
        "fanRpm",
        "fanPercent"
})
public class CpuHealth {

    @JsonProperty("temperatures")
    private double[] temperatures = null;
    @JsonProperty("voltage")
    private double voltage;
    @JsonProperty("fanRpm")
    private double fanRpm;
    @JsonProperty("fanPercent")
    private double fanPercent;

    /**
     * No args constructor for use in serialization
     */
    public CpuHealth() {
    }

    /**
     * @param fanRpm
     * @param voltage
     * @param fanPercent
     * @param temperatures
     */
    public CpuHealth(double[] temperatures, double voltage, double fanRpm, double fanPercent) {
        super();
        this.temperatures = temperatures;
        this.voltage = voltage;
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
    }

    @JsonProperty("temperatures")
    public double[] getTemperatures() {
        return temperatures;
    }

    @JsonProperty("temperatures")
    public void setTemperatures(double[] temperatures) {
        this.temperatures = temperatures;
    }

    @JsonProperty("voltage")
    public double getVoltage() {
        return voltage;
    }

    @JsonProperty("voltage")
    public void setVoltage(double voltage) {
        this.voltage = voltage;
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

}
