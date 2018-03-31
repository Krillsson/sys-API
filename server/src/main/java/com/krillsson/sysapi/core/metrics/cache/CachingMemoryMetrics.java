package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import oshi.hardware.GlobalMemory;

import java.util.concurrent.TimeUnit;

public class CachingMemoryMetrics implements MemoryMetrics {

    private final Supplier<GlobalMemory> globalMemoryCache;

    public CachingMemoryMetrics(MemoryMetrics memoryMetrics) {
        this.globalMemoryCache = Suppliers.memoizeWithExpiration(
                memoryMetrics::globalMemory,
                5,
                TimeUnit.SECONDS
        );
    }

    @Override
    public GlobalMemory globalMemory() {
        return globalMemoryCache.get();
    }
}
