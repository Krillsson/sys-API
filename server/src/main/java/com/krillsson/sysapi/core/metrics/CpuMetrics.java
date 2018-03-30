package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import oshi.hardware.CentralProcessor;

public interface CpuMetrics {
    CpuInfo cpuInfo();
    CpuLoad cpuLoad();
}
