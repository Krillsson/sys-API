package com.krillsson.sysapi.core.metrics;

public interface MetricsFactory {
    boolean prerequisitesFilled();

    boolean initialize();

    CpuMetrics cpuInfoProvider();

    NetworkMetrics networkInfoProvider();

    DriveMetrics diskInfoProvider();

    MemoryMetrics memoryInfoProvider();

    ProcessesMetrics processesInfoProvider();

    GpuMetrics gpuInfoProvider();

    MotherboardMetrics motherboardInfoProvider();
}
