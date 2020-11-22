package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics

class WindowsMetrics(
    cpuMetrics: WindowsCpuMetrics,
    networkMetrics: WindowsNetworkMetrics,
    gpuMetrics: WindowsGpuMetrics,
    driveMetrics: WindowsDriveMetrics,
    processesMetrics: DefaultProcessesMetrics,
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