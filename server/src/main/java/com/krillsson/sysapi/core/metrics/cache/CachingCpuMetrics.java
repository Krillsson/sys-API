package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.core.domain.cpu.CpuInfo;
import com.krillsson.sysapi.core.domain.cpu.CpuLoad;
import com.krillsson.sysapi.core.metrics.CpuMetrics;

import java.util.concurrent.TimeUnit;

public class CachingCpuMetrics implements CpuMetrics {

    private final Supplier<CpuInfo> cpuInfoCache;
    private final Supplier<CpuLoad> cpuLoadCache;

    CachingCpuMetrics(CpuMetrics cpuMetrics) {
        this.cpuInfoCache = Suppliers.memoizeWithExpiration(cpuMetrics::cpuInfo, 5, TimeUnit.SECONDS);
        this.cpuLoadCache = Suppliers.memoizeWithExpiration(cpuMetrics::cpuLoad, 5, TimeUnit.SECONDS);
    }

    @Override
    public CpuInfo cpuInfo() {
        return cpuInfoCache.get();
    }

    @Override
    public CpuLoad cpuLoad() {
        return cpuLoadCache.get();
    }
}
