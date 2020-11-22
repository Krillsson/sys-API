package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.metrics.DriveMetrics
import java.util.Optional

class CachingDriveMetrics(driveMetrics: DriveMetrics, cacheConfiguration: CacheConfiguration) :
    DriveMetrics {
    private val drivesCache: Supplier<List<Drive>> = Suppliers.memoizeWithExpiration(
        { driveMetrics.drives() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val driveLoadsCache: Supplier<List<DriveLoad>> = Suppliers.memoizeWithExpiration(
        { driveMetrics.driveLoads() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val driveQueryCache: LoadingCache<String, Optional<Drive>> = CacheBuilder.newBuilder()
        .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
        .build(object : CacheLoader<String, Optional<Drive>>() {
            @Throws(Exception::class)
            override fun load(s: String): Optional<Drive> {
                return driveMetrics.driveByName(s)
            }
        })
    private val driveLoadsQueryCache: LoadingCache<String, Optional<DriveLoad>> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
            .build(object : CacheLoader<String, Optional<DriveLoad>>() {
                @Throws(Exception::class)
                override fun load(s: String): Optional<DriveLoad> {
                    return driveMetrics.driveLoadByName(s)
                }
            })

    override fun drives(): List<Drive> {
        return drivesCache.get()
    }

    override fun driveLoads(): List<DriveLoad> {
        return driveLoadsCache.get()
    }

    override fun driveByName(name: String): Optional<Drive> {
        return driveQueryCache.getUnchecked(name)
    }

    override fun driveLoadByName(name: String): Optional<DriveLoad> {
        return driveLoadsQueryCache.getUnchecked(name)
    }
}