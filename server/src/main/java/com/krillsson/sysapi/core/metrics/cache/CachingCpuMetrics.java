package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.metrics.CpuMetrics;

public class CachingCpuMetrics implements CpuMetrics {

    private final Supplier<CpuInfo> cpuInfoCache;
    private final Supplier<CpuLoad> cpuLoadCache;
    private final Supplier<Long> uptimeCache;

    CachingCpuMetrics(CpuMetrics cpuMetrics, CacheConfiguration cacheConfiguration) {
        this.cpuInfoCache = Suppliers.memoizeWithExpiration(
                cpuMetrics::cpuInfo,
                cacheConfiguration.getDuration(),
                cacheConfiguration.getUnit()
        );
        this.cpuLoadCache = Suppliers.memoizeWithExpiration(
                cpuMetrics::cpuLoad,
                cacheConfiguration.getDuration(),
                cacheConfiguration.getUnit()
        );
        this.uptimeCache = Suppliers.memoizeWithExpiration(
                cpuMetrics::uptime,
                cacheConfiguration.getDuration(),
                cacheConfiguration.getUnit()
        );

    }

    @Override
    public CpuInfo cpuInfo() {
        return cpuInfoCache.get();
    }

    @Override
    public CpuLoad cpuLoad() {
        return cpuLoadCache.get();
    }

    @Override
    public long uptime() {
        return uptimeCache.get();
    }
}
