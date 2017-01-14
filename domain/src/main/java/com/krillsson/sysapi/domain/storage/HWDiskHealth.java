package com.krillsson.sysapi.domain.storage;


import com.krillsson.sysapi.domain.drive.DriveLoad;
import com.krillsson.sysapi.domain.HealthData;

import java.util.List;

public class HWDiskHealth
{
    private double temperature;
    private DriveLoad load;
    private List<HealthData> healthData;

    public HWDiskHealth(double temperature, DriveLoad load, List<HealthData> healthData)
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

    public DriveLoad getLoad() {
        return load;
    }
}
