package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.metrics.MotherboardMetrics;

import java.util.List;

public class CachingMotherboardMetrics implements MotherboardMetrics {

    private final Supplier<Motherboard> motherboardCache;
    private final Supplier<List<HealthData>> motherboardHealthCache;


    public CachingMotherboardMetrics(MotherboardMetrics motherboardMetrics, CacheConfiguration cacheConfiguration) {
        this.motherboardCache = Suppliers.memoizeWithExpiration(
                motherboardMetrics::motherboard,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
        this.motherboardHealthCache = Suppliers.memoizeWithExpiration(
                motherboardMetrics::motherboardHealth,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
    }

    @Override
    public Motherboard motherboard() {
        return motherboardCache.get();
    }

    @Override
    public List<HealthData> motherboardHealth() {
        return motherboardHealthCache.get();
    }
}
