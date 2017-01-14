package com.krillsson.sysapi.domain.storage;


import com.krillsson.sysapi.domain.drive.DriveLoad;
import com.krillsson.sysapi.domain.drive.LifecycleData;

import java.util.List;

public class HWDiskHealth
{
    private double temperature;
    private DriveLoad load;
    private List<LifecycleData> lifecycleData;

    public HWDiskHealth(double temperature, DriveLoad load, List<LifecycleData> lifecycleData)
    {
        this.temperature = temperature;
        this.load = load;
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

    public DriveLoad getLoad() {
        return load;
    }
}
