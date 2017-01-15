package com.krillsson.sysapi.domain.gpu;

import oshi.json.hardware.Display;

public class GpuInfo {
    private final Display[] displays;
    private final Gpu[] gpus;

    public GpuInfo(Display[] displays, Gpu[] gpus) {
        this.displays = displays;
        this.gpus = gpus;
    }

    public Display[] getDisplays() {
        return displays;
    }

    public Gpu[] getGpus() {
        return gpus;
    }
}
