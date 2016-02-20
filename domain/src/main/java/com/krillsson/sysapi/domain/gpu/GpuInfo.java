package com.krillsson.sysapi.domain.gpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GpuInfo
{
    private String vendor;
    private String model;
    private double coreMhz;
    private double memoryMhz;

    public GpuInfo(String vendor, String model, double coreMhz, double memoryMhz)
    {
        this.vendor = vendor;
        this.model = model;
        this.coreMhz = coreMhz;
        this.memoryMhz = memoryMhz;
    }

    @JsonProperty
    public String getVendor()
    {
        return vendor;
    }
    @JsonProperty
    public String getModel()
    {
        return model;
    }
    @JsonProperty
    public double getCoreMhz()
    {
        return coreMhz;
    }
    @JsonProperty
    public double getMemoryMhz()
    {
        return memoryMhz;
    }
}
