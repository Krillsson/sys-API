package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import oshi.hardware.GlobalMemory;

public class CachingMemoryMetrics implements MemoryMetrics {

    private final Supplier<MemoryLoad> globalMemoryCache;

    public CachingMemoryMetrics(MemoryMetrics memoryMetrics, CacheConfiguration cacheConfiguration) {
        this.globalMemoryCache = Suppliers.memoizeWithExpiration(
                memoryMetrics::memoryLoad,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
    }

    @Override
    public MemoryLoad memoryLoad() {
        return globalMemoryCache.get();
    }
}
