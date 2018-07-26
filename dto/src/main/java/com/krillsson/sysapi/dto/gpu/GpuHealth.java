package com.krillsson.sysapi.dto.gpu;

public class GpuHealth {


    private double fanRpm;

    private double fanPercent;

    private double temperature;

    public GpuHealth() {
    }

    public GpuHealth(double fanRpm, double fanPercent, double temperature) {
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
        this.temperature = temperature;
    }


    public double getFanRpm() {
        return fanRpm;
    }


    public void setFanRpm(double fanRpm) {
        this.fanRpm = fanRpm;
    }


    public double getFanPercent() {
        return fanPercent;
    }


    public void setFanPercent(double fanPercent) {
        this.fanPercent = fanPercent;
    }


    public double getTemperature() {
        return temperature;
    }


    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

}
