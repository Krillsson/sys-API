package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.GpuInfoProvider;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuInfo;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        return new GpuInfo(Arrays.asList(hal.getDisplays()), gpus());
    }

    public List<Gpu> gpus() {
        return Collections.EMPTY_LIST;
    }

}
