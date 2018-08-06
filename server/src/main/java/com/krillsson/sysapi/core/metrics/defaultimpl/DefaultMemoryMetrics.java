package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class DefaultMemoryMetrics implements MemoryMetrics {
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem operatingSystem;

    public DefaultMemoryMetrics(HardwareAbstractionLayer hal, OperatingSystem operatingSystem) {
        this.hal = hal;
        this.operatingSystem = operatingSystem;
    }

    @Override
    public MemoryLoad memoryLoad() {
        return new MemoryLoad(operatingSystem.getProcessCount(), hal.getMemory().getSwapTotal(), hal.getMemory().getSwapUsed(), hal.getMemory().getTotal(), hal.getMemory().getAvailable());
    }
}
