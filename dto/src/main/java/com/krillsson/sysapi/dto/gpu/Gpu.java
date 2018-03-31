package com.krillsson.sysapi.dto.gpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "vendor",
        "model",
        "coreMhz",
        "memoryMhz",
        "gpuHealth"
})
public class Gpu {

    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("model")
    private String model;
    @JsonProperty("coreMhz")
    private double coreMhz;
    @JsonProperty("memoryMhz")
    private double memoryMhz;

    public Gpu() {
    }

    public Gpu(String vendor, String model, double coreMhz, double memoryMhz) {
        this.vendor = vendor;
        this.model = model;
        this.coreMhz = coreMhz;
        this.memoryMhz = memoryMhz;
    }

    @JsonProperty("vendor")
    public String getVendor() {
        return vendor;
    }

    @JsonProperty("vendor")
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @JsonProperty("model")
    public String getModel() {
        return model;
    }

    @JsonProperty("model")
    public void setModel(String model) {
        this.model = model;
    }

    @JsonProperty("coreMhz")
    public double getCoreMhz() {
        return coreMhz;
    }

    @JsonProperty("coreMhz")
    public void setCoreMhz(double coreMhz) {
        this.coreMhz = coreMhz;
    }

    @JsonProperty("memoryMhz")
    public double getMemoryMhz() {
        return memoryMhz;
    }

    @JsonProperty("memoryMhz")
    public void setMemoryMhz(double memoryMhz) {
        this.memoryMhz = memoryMhz;
    }

}
