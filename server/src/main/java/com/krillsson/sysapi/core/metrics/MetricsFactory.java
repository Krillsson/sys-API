package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.system.SystemLoad;

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

    default SystemLoad consolidatedMetrics() {
        return new SystemLoad(
                cpuMetrics().uptime(), cpuMetrics().cpuLoad(),
                networkMetrics().networkInterfaceLoads(),
                driveMetrics().driveLoads(),
                memoryMetrics().memoryLoad(),
                gpuMetrics().gpuLoads(),
                motherboardMetrics().motherboardHealth());
    }
}
