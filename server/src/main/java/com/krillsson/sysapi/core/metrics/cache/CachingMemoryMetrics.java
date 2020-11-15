package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.memory.MemoryInfo;
import com.krillsson.sysapi.core.domain.memory.MemoryLoad;
import com.krillsson.sysapi.core.metrics.MemoryMetrics;
import org.jetbrains.annotations.NotNull;

public class CachingMemoryMetrics implements MemoryMetrics {

  private final Supplier<MemoryLoad> globalMemoryLoadCache;
  private final Supplier<MemoryInfo> globalMemoryInfoCache;

  public CachingMemoryMetrics(MemoryMetrics memoryMetrics, CacheConfiguration cacheConfiguration) {
    globalMemoryLoadCache = Suppliers.memoizeWithExpiration(
        memoryMetrics::memoryLoad,
        cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
    );

    globalMemoryInfoCache = Suppliers.memoizeWithExpiration(
        memoryMetrics::memoryInfo,
        cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
    );
  }

  @NotNull
  @Override
  public MemoryLoad memoryLoad() {
    return globalMemoryLoadCache.get();
  }

  @NotNull
  @Override
  public MemoryInfo memoryInfo() {
    return globalMemoryInfoCache.get();
  }
}
