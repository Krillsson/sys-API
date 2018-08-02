package com.krillsson.sysapi.core.metrics.cache;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.krillsson.sysapi.config.CacheConfiguration;
import com.krillsson.sysapi.core.domain.network.NetworkInterface;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.metrics.NetworkMetrics;

import java.util.List;
import java.util.Optional;

public class CachingNetworkMetrics implements NetworkMetrics {

    private final Supplier<List<NetworkInterface>> networkInterfacesCache;
    private final Supplier<List<NetworkInterfaceLoad>> networkInterfaceLoadsCache;
    private final LoadingCache<String, Optional<NetworkInterface>> networkInterfaceQueryCache;
    private final LoadingCache<String, Optional<NetworkInterfaceLoad>> networkInterfaceLoadQueryCache;

    public CachingNetworkMetrics(NetworkMetrics networkMetrics, CacheConfiguration cacheConfiguration) {
        this.networkInterfacesCache = Suppliers.memoizeWithExpiration(
                networkMetrics::networkInterfaces,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
        this.networkInterfaceLoadsCache = Suppliers.memoizeWithExpiration(
                networkMetrics::networkInterfaceLoads,
                cacheConfiguration.getDuration(), cacheConfiguration.getUnit()
        );
        this.networkInterfaceLoadQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheConfiguration.getDuration(), cacheConfiguration.getUnit())
                .build(new CacheLoader<String, Optional<NetworkInterfaceLoad>>() {
                    @Override
                    public Optional<NetworkInterfaceLoad> load(String s) throws Exception {
                        return networkMetrics.networkInterfaceLoadById(s);
                    }
                });
        this.networkInterfaceQueryCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheConfiguration.getDuration(), cacheConfiguration.getUnit())
                .build(new CacheLoader<String, Optional<NetworkInterface>>() {
                    @Override
                    public Optional<NetworkInterface> load(String s) throws Exception {
                        return networkMetrics.networkInterfaceById(s);
                    }
                });
    }

    @Override
    public List<NetworkInterface> networkInterfaces() {
        return networkInterfacesCache.get();
    }

    @Override
    public Optional<NetworkInterface> networkInterfaceById(String id) {
        return networkInterfaceQueryCache.getUnchecked(id);
    }

    @Override
    public List<NetworkInterfaceLoad> networkInterfaceLoads() {
        return networkInterfaceLoadsCache.get();
    }

    @Override
    public Optional<NetworkInterfaceLoad> networkInterfaceLoadById(String id) {
        return networkInterfaceLoadQueryCache.getUnchecked(id);
    }
}
