package com.krillsson.sysapi.core.metrics;

public interface MetricsFactory {
    boolean prerequisitesFilled();

    boolean initialize();

    CpuMetrics cpuMetrics();

    NetworkMetrics networkMetrics();

    DriveMetrics driveMetrics();

    MemoryMetrics memoryMetrics();

    ProcessesMetrics processesMetrics();

    GpuMetrics gpuMetrics();

    MotherboardMetrics motherboardMetrics();
}
