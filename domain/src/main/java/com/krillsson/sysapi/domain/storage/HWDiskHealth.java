package com.krillsson.sysapi.domain.storage;


import com.krillsson.sysapi.domain.health.HealthData;

import java.util.List;

public class HWDiskHealth
{
    private double temperature;
    private HWDiskLoad load;
    private List<HealthData> healthData;

    public HWDiskHealth(double temperature, HWDiskLoad load, List<HealthData> healthData)
    {
        this.temperature = temperature;
        this.load = load;
        this.healthData = healthData;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public List<HealthData> getHealthData()
    {
        return healthData;
    }

    public HWDiskLoad getLoad() {
        return load;
    }
}
