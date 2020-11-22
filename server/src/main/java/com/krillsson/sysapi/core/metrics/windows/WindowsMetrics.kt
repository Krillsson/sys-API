package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics

class WindowsMetrics(
    cpuMetrics: WindowsCpuMetrics,
    networkMetrics: WindowsNetworkMetrics,
    gpuMetrics: WindowsGpuMetrics,
    driveMetrics: WindowsDriveMetrics,
    processesMetrics: ProcessesMetrics,
    motherboardMetrics: WindowsMotherboardMetrics,
    memoryMetrics: MemoryMetrics,
    systemMetrics: SystemMetrics,
) : DefaultMetrics(
    cpuMetrics,
    networkMetrics,
    gpuMetrics,
    driveMetrics,
    processesMetrics,
    motherboardMetrics,
    memoryMetrics,
    systemMetrics
) {
    override fun initialize() {
        /* OHM has its own manager impl */
    }
}