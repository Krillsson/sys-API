package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.sensors.HealthData
import com.krillsson.sysapi.core.metrics.MotherboardMetrics

class CachingMotherboardMetrics(
    motherboardMetrics: MotherboardMetrics,
    cacheConfiguration: CacheConfiguration
) : MotherboardMetrics {
    private val motherboardCache: Supplier<Motherboard> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier{ motherboardMetrics.motherboard() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val motherboardHealthCache: Supplier<List<HealthData>> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier{ motherboardMetrics.motherboardHealth() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )

    override fun motherboard(): Motherboard {
        return motherboardCache.get()
    }

    override fun motherboardHealth(): List<HealthData> {
        return motherboardHealthCache.get()
    }
}