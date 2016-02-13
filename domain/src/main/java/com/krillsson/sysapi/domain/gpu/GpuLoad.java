package com.krillsson.sysapi.domain.gpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GpuLoad
{
    private double temperature;
    private double coreLoad;
    private double memoryLoad;

    public GpuLoad(double temperature, double coreLoad, double memoryLoad)
    {
        this.temperature = temperature;
        this.coreLoad = coreLoad;
        this.memoryLoad = memoryLoad;
    }

    @JsonProperty
    public double getTemperature()
    {
        return temperature;
    }

    @JsonProperty
    public double getCoreLoad()
    {
        return coreLoad;
    }

    @JsonProperty
    public double getMemoryLoad()
    {
        return memoryLoad;
    }
}
