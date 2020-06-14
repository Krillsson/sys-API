package com.krillsson.sysapi.core.metrics.cache

import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.core.metrics.DriveMetrics
import com.krillsson.sysapi.core.metrics.GpuMetrics
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MotherboardMetrics
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.ProcessesMetrics

class Cache private constructor(provider: Metrics, cacheConfiguration: CacheConfiguration) : Metrics {
    private val cpuMetrics: CpuMetrics
    private val networkMetrics: NetworkMetrics
    private val gpuMetrics: GpuMetrics
    private val driveMetrics: DriveMetrics
    private val processesMetrics: ProcessesMetrics
    private val motherboardMetrics: MotherboardMetrics
    private val memoryMetrics: MemoryMetrics

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

    companion object {
        fun wrap(factory: Metrics, cacheConfiguration: CacheConfiguration): Metrics {
            return Cache(factory, cacheConfiguration)
        }
    }

    init {
        cpuMetrics = CachingCpuMetrics(provider.cpuMetrics(), cacheConfiguration)
        networkMetrics = CachingNetworkMetrics(provider.networkMetrics(), cacheConfiguration)
        gpuMetrics = CachingGpuMetrics(provider.gpuMetrics(), cacheConfiguration)
        driveMetrics = CachingDriveMetrics(provider.driveMetrics(), cacheConfiguration)
        processesMetrics = CachingProcessesMetrics(provider.processesMetrics(), cacheConfiguration)
        motherboardMetrics = CachingMotherboardMetrics(provider.motherboardMetrics(), cacheConfiguration)
        memoryMetrics = CachingMemoryMetrics(provider.memoryMetrics(), cacheConfiguration)
    }
}