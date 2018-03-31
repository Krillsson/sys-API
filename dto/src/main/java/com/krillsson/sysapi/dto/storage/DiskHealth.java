package com.krillsson.sysapi.dto.storage;

import com.krillsson.sysapi.dto.sensors.HealthData;


public class DiskHealth {


    private double temperature;

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


    public double getTemperature() {
        return temperature;
    }


    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


    public HealthData[] getHealthData() {
        return healthData;
    }


    public void setHealthData(HealthData[] healthData) {
        this.healthData = healthData;
    }

}
