
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
    private Integer fanRpm;
    @JsonProperty("fanPercent")
    private Integer fanPercent;
    @JsonProperty("temperature")
    private Integer temperature;
    @JsonProperty("coreLoad")
    private Integer coreLoad;
    @JsonProperty("memoryLoad")
    private Integer memoryLoad;

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

    @JsonProperty("temperature")
    public Integer getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("coreLoad")
    public Integer getCoreLoad() {
        return coreLoad;
    }

    @JsonProperty("coreLoad")
    public void setCoreLoad(Integer coreLoad) {
        this.coreLoad = coreLoad;
    }

    @JsonProperty("memoryLoad")
    public Integer getMemoryLoad() {
        return memoryLoad;
    }

    @JsonProperty("memoryLoad")
    public void setMemoryLoad(Integer memoryLoad) {
        this.memoryLoad = memoryLoad;
    }

}
