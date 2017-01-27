package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "temperature",
        "healthData"
})
public class DiskHealth {

    @JsonProperty("temperature")
    private Integer temperature;
    @JsonProperty("healthData")
    private HealthData[] healthData = null;

    /**
     * No args constructor for use in serialization
     */
    public DiskHealth() {
    }

    /**
     * @param healthData
     * @param temperature
     */
    public DiskHealth(Integer temperature, HealthData[] healthData) {
        this.temperature = temperature;
        this.healthData = healthData;
    }

    @JsonProperty("temperature")
    public Integer getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("healthData")
    public HealthData[] getHealthData() {
        return healthData;
    }

    @JsonProperty("healthData")
    public void setHealthData(HealthData[] healthData) {
        this.healthData = healthData;
    }

}
