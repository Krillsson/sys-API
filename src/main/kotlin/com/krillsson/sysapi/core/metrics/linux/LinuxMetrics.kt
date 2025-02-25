package com.krillsson.sysapi.core.metrics.linux

import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
@Lazy
class LinuxMetrics(
    cpuMetrics: LinuxCpuMetrics,
    networkMetrics: DefaultNetworkMetrics,
    @Qualifier("defaultGpuMetrics") gpuMetrics: DefaultGpuMetrics,
    diskMetrics: DefaultDiskMetrics,
    fileSystemMetrics: DefaultFileSystemMetrics,
    processesMetrics: DefaultProcessesMetrics,
    @Qualifier("defaultMotherboardMetrics") motherboardMetrics: DefaultMotherboardMetrics,
    memoryMetrics: MemoryMetrics,
    systemMetrics: SystemMetrics
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