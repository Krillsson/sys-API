package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;

public interface CpuMetrics {
    CpuInfo cpuInfo();

    CpuLoad cpuLoad();

    long uptime();
}
