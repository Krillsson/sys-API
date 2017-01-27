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
    private int[] temperatures = null;
    @JsonProperty("voltage")
    private Integer voltage;
    @JsonProperty("fanRpm")
    private Integer fanRpm;
    @JsonProperty("fanPercent")
    private Integer fanPercent;

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
    public CpuHealth(int[] temperatures, Integer voltage, Integer fanRpm, Integer fanPercent) {
        super();
        this.temperatures = temperatures;
        this.voltage = voltage;
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
    }

    @JsonProperty("temperatures")
    public int[] getTemperatures() {
        return temperatures;
    }

    @JsonProperty("temperatures")
    public void setTemperatures(int[] temperatures) {
        this.temperatures = temperatures;
    }

    @JsonProperty("voltage")
    public Integer getVoltage() {
        return voltage;
    }

    @JsonProperty("voltage")
    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    @JsonProperty("fanRpm")
    public Integer getFanRpm() {
        return fanRpm;
    }

    @JsonProperty("fanRpm")
    public void setFanRpm(Integer fanRpm) {
        this.fanRpm = fanRpm;
    }

    @JsonProperty("fanPercent")
    public Integer getFanPercent() {
        return fanPercent;
    }

    @JsonProperty("fanPercent")
    public void setFanPercent(Integer fanPercent) {
        this.fanPercent = fanPercent;
    }

}
