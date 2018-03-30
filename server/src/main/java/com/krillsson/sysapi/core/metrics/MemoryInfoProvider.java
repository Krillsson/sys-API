package com.krillsson.sysapi.core.metrics;

import oshi.hardware.GlobalMemory;

public interface MemoryInfoProvider {
    GlobalMemory globalMemory();
}
