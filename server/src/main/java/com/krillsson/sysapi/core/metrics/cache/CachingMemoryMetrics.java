package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import oshi.hardware.GlobalMemory;

public class CachingMemoryMetrics implements MemoryMetrics {

    private final Supplier<GlobalMemory> globalMemoryCache;

    public CachingMemoryMetrics(MemoryMetrics memoryMetrics, CacheConfiguration cacheConfiguration) {
        this.globalMemoryCache = Suppliers.memoizeWithExpiration(
                memoryMetrics::globalMemory,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
    }

    @Override
    public GlobalMemory globalMemory() {
        return globalMemoryCache.get();
    }
}
