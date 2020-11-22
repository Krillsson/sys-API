package com.krillsson.sysapi.core.metrics.cache

import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.core.metrics.DriveMetrics
import com.krillsson.sysapi.core.metrics.GpuMetrics
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MotherboardMetrics
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import com.krillsson.sysapi.core.metrics.SystemMetrics

class Cache private constructor(
    metrics: Metrics,
    cacheConfiguration: CacheConfiguration,
    platform: Platform,
    operatingSystem: OperatingSystem
) : Metrics {
    private val cpuMetrics: CpuMetrics
    private val networkMetrics: NetworkMetrics
    private val gpuMetrics: GpuMetrics
    private val driveMetrics: DriveMetrics
    private val processesMetrics: ProcessesMetrics
    private val motherboardMetrics: MotherboardMetrics
    private val memoryMetrics: MemoryMetrics
    private val systemMetrics: SystemMetrics

    override fun initialize() {}

    override fun cpuMetrics(): CpuMetrics {
        return cpuMetrics
    }

    override fun networkMetrics(): NetworkMetrics {
        return networkMetrics
    }

    override fun driveMetrics(): DriveMetrics {
        return driveMetrics
    }

    override fun memoryMetrics(): MemoryMetrics {
        return memoryMetrics
    }

    override fun processesMetrics(): ProcessesMetrics {
        return processesMetrics
    }

    override fun gpuMetrics(): GpuMetrics {
        return gpuMetrics
    }

    override fun motherboardMetrics(): MotherboardMetrics {
        return motherboardMetrics
    }

    override fun systemMetrics(): SystemMetrics {
        return systemMetrics
    }

    companion object {
        fun wrap(
            factory: Metrics,
            cacheConfiguration: CacheConfiguration,
            platform: Platform,
            operatingSystem: OperatingSystem
        ): Metrics {
            return Cache(factory, cacheConfiguration, platform, operatingSystem)
        }
    }

    init {
        cpuMetrics = CachingCpuMetrics(metrics.cpuMetrics(), cacheConfiguration)
        networkMetrics = CachingNetworkMetrics(metrics.networkMetrics(), cacheConfiguration)
        gpuMetrics = CachingGpuMetrics(metrics.gpuMetrics(), cacheConfiguration)
        driveMetrics = CachingDriveMetrics(metrics.driveMetrics(), cacheConfiguration)
        processesMetrics = CachingProcessesMetrics(metrics.processesMetrics(), cacheConfiguration)
        motherboardMetrics = CachingMotherboardMetrics(metrics.motherboardMetrics(), cacheConfiguration)
        memoryMetrics = CachingMemoryMetrics(metrics.memoryMetrics(), cacheConfiguration)
        systemMetrics = CachingSystemMetrics(
            cpuMetrics,
            networkMetrics,
            driveMetrics,
            memoryMetrics,
            processesMetrics,
            motherboardMetrics,
            gpuMetrics,
            platform,
            operatingSystem
        )
    }
}