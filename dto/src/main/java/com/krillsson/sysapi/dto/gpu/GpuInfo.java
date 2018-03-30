package com.krillsson.sysapi.dto.gpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "displays",
        "gpus"
})
public class GpuInfo {

    @JsonProperty("displays")
    private List<Display> displays = null;
    @JsonProperty("gpus")
    private List<Gpu> gpus = null;

    public GpuInfo() {
    }

    public GpuInfo(List<Display> displays, List<Gpu> gpus) {
        this.displays = displays;
        this.gpus = gpus;
    }

    @JsonProperty("displays")
    public List<Display> getDisplays() {
        return displays;
    }

    @JsonProperty("displays")
    public void setDisplays(List<Display> displays) {
        this.displays = displays;
    }

    @JsonProperty("gpus")
    public List<Gpu> getGpus() {
        return gpus;
    }

    @JsonProperty("gpus")
    public void setGpus(List<Gpu> gpus) {
        this.gpus = gpus;
    }

}
