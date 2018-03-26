package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuHealth;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultGpuInfoProvider implements GpuInfoProvider {

    private final HardwareAbstractionLayer hal;

    public DefaultGpuInfoProvider(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @Override
    public List<GpuLoad> gpuLoads() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public GpuInfo gpuInfo() {
        return new GpuInfo(hal.getDisplays(), gpus());
    }

    public Gpu[] gpus() {
        return new Gpu[0];
    }

}
