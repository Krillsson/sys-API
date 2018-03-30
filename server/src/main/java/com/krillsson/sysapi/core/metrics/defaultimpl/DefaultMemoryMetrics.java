package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class DefaultMemoryMetrics implements MemoryMetrics {
    private final HardwareAbstractionLayer hal;

    public DefaultMemoryMetrics(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @Override
    public GlobalMemory globalMemory() {
        return hal.getMemory();
    }
}
