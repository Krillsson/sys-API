package com.krillsson.sysapi.dto.gpu;

public class Gpu {


    private String vendor;

    private String model;

    private double coreMhz;

    private double memoryMhz;

    public Gpu() {
    }

    public Gpu(String vendor, String model, double coreMhz, double memoryMhz) {
        this.vendor = vendor;
        this.model = model;
        this.coreMhz = coreMhz;
        this.memoryMhz = memoryMhz;
    }


    public String getVendor() {
        return vendor;
    }


    public void setVendor(String vendor) {
        this.vendor = vendor;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public double getCoreMhz() {
        return coreMhz;
    }


    public void setCoreMhz(double coreMhz) {
        this.coreMhz = coreMhz;
    }


    public double getMemoryMhz() {
        return memoryMhz;
    }


    public void setMemoryMhz(double memoryMhz) {
        this.memoryMhz = memoryMhz;
    }

}
