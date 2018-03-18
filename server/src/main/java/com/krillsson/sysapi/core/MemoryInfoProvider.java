package com.krillsson.sysapi.core;

import oshi.hardware.GlobalMemory;

public interface MemoryInfoProvider {
    GlobalMemory globalMemory();
}
