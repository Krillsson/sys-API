package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.metrics.DriveMetrics;

import java.util.List;
import java.util.Optional;

public class CachingDriveMetrics implements DriveMetrics {
    private final Supplier<List<Drive>> drivesCache;
    private final Supplier<List<DriveLoad>> driveLoadsCache;
    private final LoadingCache<String, Optional<Drive>> driveQueryCache;
    private final LoadingCache<String, Optional<DriveLoad>> driveLoadsQueryCache;

    public CachingDriveMetrics(DriveMetrics driveMetrics, CacheConfiguration cacheConfiguration) {
        this.drivesCache = Suppliers.memoizeWithExpiration(
                driveMetrics::drives,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );

        this.driveLoadsCache = Suppliers.memoizeWithExpiration(
                driveMetrics::driveLoads,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
        this.driveQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheConfiguration.getDuration(), cacheConfiguration.getUnit())
                .build(new CacheLoader<String, Optional<Drive>>() {
                    @Override
                    public Optional<Drive> load(String s) throws Exception {
                        return driveMetrics.driveByName(s);
                    }
                });
        this.driveLoadsQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheConfiguration.getDuration(), cacheConfiguration.getUnit())
                .build(new CacheLoader<String, Optional<DriveLoad>>() {
                    @Override
                    public Optional<DriveLoad> load(String s) throws Exception {
                        return driveMetrics.driveLoadByName(s);
                    }
                });
    }

    @Override
    public List<Drive> drives() {
        return drivesCache.get();
    }

    @Override
    public List<DriveLoad> driveLoads() {
        return driveLoadsCache.get();
    }

    @Override
    public Optional<Drive> driveByName(String name) {
        return driveQueryCache.getUnchecked(name);
    }

    @Override
    public Optional<DriveLoad> driveLoadByName(String name) {
        return driveLoadsQueryCache.getUnchecked(name);
    }

}
