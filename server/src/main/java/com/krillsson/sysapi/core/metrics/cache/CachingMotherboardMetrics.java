package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
import com.krillsson.sysapi.core.domain.sensors.HealthData;
import com.krillsson.sysapi.core.metrics.MotherboardMetrics;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachingMotherboardMetrics implements MotherboardMetrics {

    private final Supplier<Motherboard> motherboardCache;
    private final Supplier<List<HealthData>> motherboardHealthCache;


    public CachingMotherboardMetrics(MotherboardMetrics motherboardMetrics) {
        this.motherboardCache = Suppliers.memoizeWithExpiration(
                motherboardMetrics::motherboard,
                5,
                TimeUnit.SECONDS
        );
        this.motherboardHealthCache = Suppliers.memoizeWithExpiration(
                motherboardMetrics::motherboardHealth,
                5,
                TimeUnit.SECONDS
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
