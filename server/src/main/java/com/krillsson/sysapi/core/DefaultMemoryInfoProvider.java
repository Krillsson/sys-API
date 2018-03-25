package com.krillsson.sysapi.core;

import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

public class DefaultMemoryInfoProvider implements MemoryInfoProvider {
    private final HardwareAbstractionLayer hal;

    public DefaultMemoryInfoProvider(HardwareAbstractionLayer hal) {
        this.hal = hal;
    }

    @Override
    public GlobalMemory globalMemory() {
        return hal.getMemory();
    }
}
