package com.krillsson.sysapi.dto.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.krillsson.sysapi.dto.sensors.HealthData;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "temperature",
        "healthData"
})
public class DiskHealth {

    @JsonProperty("temperature")
    private double temperature;
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
    public DiskHealth(double temperature, HealthData[] healthData) {
        this.temperature = temperature;
        this.healthData = healthData;
    }

    @JsonProperty("temperature")
    public double getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature")
    public void setTemperature(double temperature) {
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
