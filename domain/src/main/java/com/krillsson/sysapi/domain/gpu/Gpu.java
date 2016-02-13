package com.krillsson.sysapi.domain.gpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gpu
{
    private double fanRpm;
    private double fanPercent;
    private GpuInfo info;
    private GpuLoad load;

    public Gpu(double fanRpm, double fanPercent, GpuInfo info, GpuLoad load)
    {
        this.fanRpm = fanRpm;
        this.fanPercent = fanPercent;
        this.info = info;
        this.load = load;
    }

    @JsonProperty
    public double getFanRpm()
    {
        return fanRpm;
    }
    @JsonProperty
    public double getFanPercent()
    {
        return fanPercent;
    }
    @JsonProperty
    public GpuInfo getInfo()
    {
        return info;
    }
    @JsonProperty
    public GpuLoad getLoad()
    {
        return load;
    }
}
