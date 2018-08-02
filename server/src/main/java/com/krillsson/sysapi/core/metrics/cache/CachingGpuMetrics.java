package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.gpu.Gpu;
import com.krillsson.sysapi.core.domain.gpu.GpuLoad;
import com.krillsson.sysapi.core.metrics.GpuMetrics;
import oshi.hardware.Display;

import java.util.List;

public class CachingGpuMetrics implements GpuMetrics {
    private final Supplier<List<Gpu>> gpusCache;
    private final Supplier<List<Display>> displaysCache;
    private final Supplier<List<GpuLoad>> gpuLoadsCache;

    public CachingGpuMetrics(GpuMetrics gpuMetrics, CacheConfiguration cacheConfiguration) {
        this.gpusCache = Suppliers.memoizeWithExpiration(
                gpuMetrics::gpus,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
        this.displaysCache = Suppliers.memoizeWithExpiration(
                gpuMetrics::displays,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
        this.gpuLoadsCache = Suppliers.memoizeWithExpiration(
                gpuMetrics::gpuLoads,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
    }

    @Override
    public List<Gpu> gpus() {
        return gpusCache.get();
    }

    @Override
    public List<Display> displays() {
        return displaysCache.get();
    }

    @Override
    public List<GpuLoad> gpuLoads() {
        return gpuLoadsCache.get();
    }

}
