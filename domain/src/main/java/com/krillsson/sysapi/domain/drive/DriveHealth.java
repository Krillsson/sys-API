package com.krillsson.sysapi.domain.drive;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class DriveHealth
{
    private double temperature;
    private double remainingLifePercent;
    private List<LifecycleData> lifecycleData;

    public DriveHealth(double temperature, double remainingLifePercent, List<LifecycleData> lifecycleData)
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
    public List<LifecycleData> getLifecycleData()
    {
        return lifecycleData;
    }
}
