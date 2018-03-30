package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import oshi.hardware.CentralProcessor;

public interface CpuInfoProvider {
    CpuInfo cpuInfo();
    CpuLoad cpuLoad();
}
