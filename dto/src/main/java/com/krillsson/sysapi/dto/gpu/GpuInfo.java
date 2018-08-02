package com.krillsson.sysapi.dto.gpu;

import java.util.List;

public class GpuInfo {


    private List<Display> displays = null;

    private List<Gpu> gpus = null;

    public GpuInfo() {
    }

    public GpuInfo(List<Display> displays, List<Gpu> gpus) {
        this.displays = displays;
        this.gpus = gpus;
    }


    public List<Display> getDisplays() {
        return displays;
    }


    public void setDisplays(List<Display> displays) {
        this.displays = displays;
    }


    public List<Gpu> getGpus() {
        return gpus;
    }


    public void setGpus(List<Gpu> gpus) {
        this.gpus = gpus;
    }

}
