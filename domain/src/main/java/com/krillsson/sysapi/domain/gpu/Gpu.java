package com.krillsson.sysapi.domain.gpu;

public class Gpu
{
    private String vendor;
    private String model;
    private double coreMhz;
    private double memoryMhz;
    private GpuLoad load;

    public Gpu(String vendor, String model, double coreMhz, double memoryMhz, GpuLoad load)
    {
        this.vendor = vendor;
        this.model = model;
        this.coreMhz = coreMhz;
        this.memoryMhz = memoryMhz;
        this.load = load;
    }

    public String getVendor() {
        return vendor;
    }

    public String getModel() {
        return model;
    }

    public double getCoreMhz() {
        return coreMhz;
    }

    public double getMemoryMhz() {
        return memoryMhz;
    }

    public GpuLoad getLoad() {
        return load;
    }
}
