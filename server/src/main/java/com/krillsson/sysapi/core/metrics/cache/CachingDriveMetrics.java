package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;
import com.krillsson.sysapi.core.metrics.DriveMetrics;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CachingDriveMetrics implements DriveMetrics {
    private final Supplier<List<Drive>> drivesCache;
    private final Supplier<List<DriveLoad>> driveLoadsCache;
    private final LoadingCache<String, Optional<Drive>> driveQueryCache;
    private final LoadingCache<String, Optional<DriveLoad>> driveLoadsQueryCache;

    public CachingDriveMetrics(DriveMetrics driveMetrics) {
        this.drivesCache = Suppliers.memoizeWithExpiration(
                driveMetrics::drives,
                5,
                TimeUnit.SECONDS
        );

        this.driveLoadsCache = Suppliers.memoizeWithExpiration(
                driveMetrics::driveLoads,
                5,
                TimeUnit.SECONDS
        );
        this.driveQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Optional<Drive>>() {
                    @Override
                    public Optional<Drive> load(String s) throws Exception {
                        return driveMetrics.driveByName(s);
                    }
                });
        this.driveLoadsQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
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
