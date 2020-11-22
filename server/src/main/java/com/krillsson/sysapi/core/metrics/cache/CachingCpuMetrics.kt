package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.metrics.CpuMetrics

class CachingCpuMetrics internal constructor(
    cpuMetrics: CpuMetrics,
    cacheConfiguration: CacheConfiguration
) : CpuMetrics {
    private val cpuInfoCache: Supplier<CpuInfo> = Suppliers.memoizeWithExpiration(
        { cpuMetrics.cpuInfo() },
        cacheConfiguration.duration,
        cacheConfiguration.unit
    )
    private val cpuLoadCache: Supplier<CpuLoad> = Suppliers.memoizeWithExpiration(
        { cpuMetrics.cpuLoad() },
        cacheConfiguration.duration,
        cacheConfiguration.unit
    )
    private val uptimeCache: Supplier<Long> = Suppliers.memoizeWithExpiration(
        { cpuMetrics.uptime() },
        cacheConfiguration.duration,
        cacheConfiguration.unit
    )

    override fun cpuInfo(): CpuInfo {
        return cpuInfoCache.get()
    }

    override fun cpuLoad(): CpuLoad {
        return cpuLoadCache.get()
    }

    override fun uptime(): Long {
        return uptimeCache.get()
    }
}