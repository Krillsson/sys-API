package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.*

class RaspbianMetrics(
    cpuMetrics: DefaultCpuMetrics,
    networkMetrics: DefaultNetworkMetrics,
    gpuMetrics: DefaultGpuMetrics,
    driveMetrics: DefaultDriveMetrics,
    diskMetrics: DefaultDiskMetrics,
    fileSystemMetrics: DefaultFileSystemMetrics,
    processesMetrics: DefaultProcessesMetrics,
    motherboardMetrics: DefaultMotherboardMetrics,
    memoryMetrics: DefaultMemoryMetrics,
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
)