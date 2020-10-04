package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
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
        GlobalMemory memory = hal.getMemory();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        return new MemoryLoad(operatingSystem.getProcessCount(), virtualMemory.getSwapTotal(), virtualMemory.getSwapUsed(), memory.getTotal(), memory.getAvailable(), usedPercent(memory));
    }

    private int usedPercent(GlobalMemory memory) {
        long free = memory.getAvailable();
        long total = memory.getTotal();
        long used = total - free;
        return (int) (used * 100.0 / (total) + 0.5);
    }

}
