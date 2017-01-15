package com.krillsson.sysapi.domain.storage;


import com.krillsson.sysapi.domain.health.HealthData;

import java.util.List;

public class HWDiskHealth {
    private double temperature;
    private List<HealthData> healthData;

    public HWDiskHealth(double temperature, List<HealthData> healthData) {
        this.temperature = temperature;
        this.healthData = healthData;
    }

    public double getTemperature() {
        return temperature;
    }

    public List<HealthData> getHealthData() {
        return healthData;
    }

}
