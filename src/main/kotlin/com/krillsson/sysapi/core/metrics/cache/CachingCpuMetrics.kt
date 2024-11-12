package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.metrics.CpuMetrics
import reactor.core.publisher.Flux

class CachingCpuMetrics internal constructor(
    private val cpuMetrics: CpuMetrics,
    cacheConfiguration: CacheConfiguration
) : CpuMetrics {

    private val cpuInfoCache: Supplier<CpuInfo> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier { cpuMetrics.cpuInfo() },
        cacheConfiguration.duration,
        cacheConfiguration.unit
    )
    private val cpuLoadCache: Supplier<CpuLoad> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier { cpuMetrics.cpuLoad() },
        cacheConfiguration.duration,
        cacheConfiguration.unit
    )
    private val uptimeCache: Supplier<Long> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier { cpuMetrics.uptime() },
        cacheConfiguration.duration,
        cacheConfiguration.unit
    )

    override fun cpuInfo(): CpuInfo {
        return cpuInfoCache.get()
    }

    override fun cpuLoad(): CpuLoad {
        return cpuLoadCache.get()
    }

    override fun cpuLoadEvents(): Flux<CpuLoad> {
        return cpuMetrics.cpuLoadEvents()
    }

    override fun uptime(): Long {
        return uptimeCache.get()
    }
}