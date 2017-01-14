package com.krillsson.sysapi.domain.storage;


import com.krillsson.sysapi.domain.drive.LifecycleData;

import java.util.List;

public class HWDiskHealth
{
    private double temperature;
    private List<LifecycleData> lifecycleData;

    public HWDiskHealth(double temperature, List<LifecycleData> lifecycleData)
    {
        this.temperature = temperature;
        this.lifecycleData = lifecycleData;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public List<LifecycleData> getLifecycleData()
    {
        return lifecycleData;
    }
}
