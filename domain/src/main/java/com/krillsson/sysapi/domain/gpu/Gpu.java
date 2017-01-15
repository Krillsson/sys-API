package com.krillsson.sysapi.domain.gpu;

public class Gpu {
    private String vendor;
    private String model;
    private double coreMhz;
    private double memoryMhz;
    private GpuHealth load;

    public Gpu(String vendor, String model, double coreMhz, double memoryMhz, GpuHealth load) {
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

    public GpuHealth getLoad() {
        return load;
    }
}
