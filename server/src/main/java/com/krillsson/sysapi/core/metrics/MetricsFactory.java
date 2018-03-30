package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.metrics.*;

public interface MetricsFactory {
    boolean prerequisitesFilled();

    boolean initialize();

    CpuMetrics cpuInfoProvider();

    NetworkMetrics networkInfoProvider();

    DiskMetrics diskInfoProvider();

    MemoryMetrics memoryInfoProvider();

    ProcessesMetrics processesInfoProvider();

    GpuMetrics gpuInfoProvider();

    MotherboardMetrics motherboardInfoProvider();
}
