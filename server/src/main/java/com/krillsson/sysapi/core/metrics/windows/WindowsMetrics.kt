package com.krillsson.sysapi.core.metrics.windows

import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDiskMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultFileSystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics

class WindowsMetrics(
    cpuMetrics: WindowsCpuMetrics,
    networkMetrics: WindowsNetworkMetrics,
    gpuMetrics: WindowsGpuMetrics,
    driveMetrics: WindowsDriveMetrics,
    diskMetrics: DefaultDiskMetrics,
    fileSystemMetrics: DefaultFileSystemMetrics,
    processesMetrics: DefaultProcessesMetrics,
    motherboardMetrics: WindowsMotherboardMetrics,
    memoryMetrics: MemoryMetrics,
    systemMetrics: SystemMetrics,
) : DefaultMetrics(
    cpuMetrics,
    networkMetrics,
    gpuMetrics,
    driveMetrics,
    diskMetrics,
    fileSystemMetrics,
    processesMetrics,
    motherboardMetrics,
    memoryMetrics,
    systemMetrics
) {
    override fun initialize() {
        /* OHM has its own manager impl */
    }
}