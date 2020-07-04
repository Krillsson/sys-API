package com.krillsson.sysapi.core.metrics.cache

import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultSystemMetrics

class CachingSystemMetrics(
    cpuMetrics: CachingCpuMetrics,
    networkMetrics: CachingNetworkMetrics,
    driveMetrics: CachingDriveMetrics,
    memoryMetrics: CachingMemoryMetrics,
    processesMetrics: CachingProcessesMetrics,
    motherboardMetrics: CachingMotherboardMetrics,
    gpuMetrics: CachingGpuMetrics,
    platform: Platform,
    operatingSystem: OperatingSystem
) : DefaultSystemMetrics(
    cpuMetrics,
    networkMetrics,
    driveMetrics,
    memoryMetrics,
    processesMetrics,
    motherboardMetrics,
    gpuMetrics,
    operatingSystem,
    platform
)