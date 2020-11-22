package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultCpuMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultDriveMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultGpuMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMemoryMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMotherboardMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultProcessesMetrics

class RaspbianMetrics(
    cpuMetrics: DefaultCpuMetrics,
    networkMetrics: DefaultNetworkMetrics,
    gpuMetrics: DefaultGpuMetrics,
    driveMetrics: DefaultDriveMetrics,
    processesMetrics: DefaultProcessesMetrics,
    motherboardMetrics: DefaultMotherboardMetrics,
    memoryMetrics: DefaultMemoryMetrics,
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
)