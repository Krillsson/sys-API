package com.krillsson.sysapi.domain.filesystem;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DriveHealth
{
    private double temperature;
    private double remainingLifePercent;
    private Map<String, String> lifecycleData;

    public DriveHealth(double temperature, double remainingLifePercent, Map<String, String> lifecycleData)
    {
        this.temperature = temperature;
        this.remainingLifePercent = remainingLifePercent;
        this.lifecycleData = lifecycleData;
    }

    @JsonProperty
    public double getTemperature()
    {
        return temperature;
    }
    @JsonProperty
    public double getRemainingLifePercent()
    {
        return remainingLifePercent;
    }
    @JsonProperty
    public Map<String, String> getLifecycleData()
    {
        return lifecycleData;
    }
}
