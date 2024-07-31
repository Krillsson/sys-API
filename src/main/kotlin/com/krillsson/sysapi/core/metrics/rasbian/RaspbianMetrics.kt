package com.krillsson.sysapi.core.metrics.rasbian

import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class RaspbianMetrics(
    @Qualifier("defaultCpuMetrics") cpuMetrics: DefaultCpuMetrics,
    networkMetrics: DefaultNetworkMetrics,
    @Qualifier("defaultGpuMetrics") gpuMetrics: DefaultGpuMetrics,
    diskMetrics: DefaultDiskMetrics,
    fileSystemMetrics: DefaultFileSystemMetrics,
    processesMetrics: DefaultProcessesMetrics,
    @Qualifier("defaultMotherboardMetrics") motherboardMetrics: DefaultMotherboardMetrics,
    memoryMetrics: DefaultMemoryMetrics,
    systemMetrics: SystemMetrics,
) : DefaultMetrics(
    cpuMetrics,
    networkMetrics,
    gpuMetrics,
    diskMetrics,
    fileSystemMetrics,
    processesMetrics,
    motherboardMetrics,
    memoryMetrics,
    systemMetrics
)