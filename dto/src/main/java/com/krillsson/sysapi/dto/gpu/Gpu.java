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
    private Integer coreMhz;
    @JsonProperty("memoryMhz")
    private Integer memoryMhz;
    @JsonProperty("gpuHealth")
    private GpuHealth gpuHealth;

    public Gpu() {
    }

    public Gpu(String vendor, String model, Integer coreMhz, Integer memoryMhz, GpuHealth gpuHealth) {
        this.vendor = vendor;
        this.model = model;
        this.coreMhz = coreMhz;
        this.memoryMhz = memoryMhz;
        this.gpuHealth = gpuHealth;
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
    public Integer getCoreMhz() {
        return coreMhz;
    }

    @JsonProperty("coreMhz")
    public void setCoreMhz(Integer coreMhz) {
        this.coreMhz = coreMhz;
    }

    @JsonProperty("memoryMhz")
    public Integer getMemoryMhz() {
        return memoryMhz;
    }

    @JsonProperty("memoryMhz")
    public void setMemoryMhz(Integer memoryMhz) {
        this.memoryMhz = memoryMhz;
    }

    @JsonProperty("gpuHealth")
    public GpuHealth getGpuHealth() {
        return gpuHealth;
    }

    @JsonProperty("gpuHealth")
    public void setGpuHealth(GpuHealth gpuHealth) {
        this.gpuHealth = gpuHealth;
    }

}
