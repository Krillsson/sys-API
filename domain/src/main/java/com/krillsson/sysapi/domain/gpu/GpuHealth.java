package com.krillsson.sysapi.domain.gpu;

public class GpuHealth
{
    private double fanRpm;
    private double fanPercent;
    private double temperature;
    private double coreLoad;
    private double memoryLoad;

    public GpuHealth(double fanRpm, double fanPercent, double temperature, double coreLoad, double memoryLoad)
    {
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
        this.temperature = temperature;
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
    }

    public double getFanRpm() {
        return fanRpm;
    }

    public double getFanPercent() {
        return fanPercent;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public double getCoreLoad()
    {
        return coreLoad;
    }

    public double getMemoryLoad()
    {
        return memoryLoad;
    }
}
