package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.metrics.GpuMetrics
import oshi.hardware.Display

class CachingGpuMetrics(
    gpuMetrics: GpuMetrics,
    cacheConfiguration: CacheConfiguration
) : GpuMetrics {
    private val gpusCache: Supplier<List<Gpu>> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier { gpuMetrics.gpus() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val displaysCache: Supplier<List<Display>> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier { gpuMetrics.displays() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val gpuLoadsCache: Supplier<List<GpuLoad>> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier { gpuMetrics.gpuLoads() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )

    override fun gpus(): List<Gpu> {
        return gpusCache.get()
    }

    override fun displays(): List<Display> {
        return displaysCache.get()
    }

    override fun gpuLoads(): List<GpuLoad> {
        return gpuLoadsCache.get()
    }
}