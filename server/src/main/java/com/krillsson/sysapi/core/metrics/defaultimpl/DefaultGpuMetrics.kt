package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.metrics.GpuMetrics;
import oshi.hardware.Display;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultGpuMetrics implements GpuMetrics {

    private final HardwareAbstractionLayer hal;

    public DefaultGpuMetrics(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @Override
    public List<GpuLoad> gpuLoads() {
        return Collections.emptyList();
    }

    public List<Gpu> gpus() {
        return Collections.emptyList();
    }

    @Override
    public List<Display> displays() {
        return hal.getDisplays();
    }

}
