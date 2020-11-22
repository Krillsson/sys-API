package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.memory.MemoryInfo
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.metrics.MemoryMetrics

class CachingMemoryMetrics(memoryMetrics: MemoryMetrics, cacheConfiguration: CacheConfiguration) :
    MemoryMetrics {
    private val globalMemoryLoadCache: Supplier<MemoryLoad> = Suppliers.memoizeWithExpiration(
        { memoryMetrics.memoryLoad() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val globalMemoryInfoCache: Supplier<MemoryInfo> = Suppliers.memoizeWithExpiration(
        { memoryMetrics.memoryInfo() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )

    override fun memoryLoad(): MemoryLoad {
        return globalMemoryLoadCache.get()
    }

    override fun memoryInfo(): MemoryInfo {
        return globalMemoryInfoCache.get()
    }
}