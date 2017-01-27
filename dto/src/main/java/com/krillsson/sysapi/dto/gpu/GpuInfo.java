package com.krillsson.sysapi.dto.gpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "displays",
        "gpus"
})
public class GpuInfo {

    @JsonProperty("displays")
    private Display[] displays = null;
    @JsonProperty("gpus")
    private Gpu[] gpus = null;

    public GpuInfo() {
    }

    public GpuInfo(Display[] displays, Gpu[] gpus) {
        this.displays = displays;
        this.gpus = gpus;
    }

    @JsonProperty("displays")
    public Display[] getDisplays() {
        return displays;
    }

    @JsonProperty("displays")
    public void setDisplays(Display[] displays) {
        this.displays = displays;
    }

    @JsonProperty("gpus")
    public Gpu[] getGpus() {
        return gpus;
    }

    @JsonProperty("gpus")
    public void setGpus(Gpu[] gpus) {
        this.gpus = gpus;
    }

}
