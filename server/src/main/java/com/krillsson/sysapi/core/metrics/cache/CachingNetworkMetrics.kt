package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import java.util.*

class CachingNetworkMetrics(
    networkMetrics: NetworkMetrics,
    cacheConfiguration: CacheConfiguration
) : NetworkMetrics {
    private val networkInterfacesCache: Supplier<List<NetworkInterface>> =
        Suppliers.memoizeWithExpiration(
            Suppliers.synchronizedSupplier{ networkMetrics.networkInterfaces() },
            cacheConfiguration.duration, cacheConfiguration.unit
        )
    private val networkInterfaceLoadsCache: Supplier<List<NetworkInterfaceLoad>> =
        Suppliers.memoizeWithExpiration(
            Suppliers.synchronizedSupplier{ networkMetrics.networkInterfaceLoads() },
            cacheConfiguration.duration, cacheConfiguration.unit
        )
    private val connectivityCache: Supplier<Connectivity> = Suppliers.memoizeWithExpiration(
        Suppliers.synchronizedSupplier{ networkMetrics.connectivity() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val networkInterfaceQueryCache: LoadingCache<String, Optional<NetworkInterface>> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
            .build(object : CacheLoader<String, Optional<NetworkInterface>>() {
                @Throws(Exception::class)
                override fun load(s: String): Optional<NetworkInterface> {
                    return networkMetrics.networkInterfaceById(s)
                }
            })
    private val networkInterfaceLoadQueryCache: LoadingCache<String, Optional<NetworkInterfaceLoad>> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
            .build(object : CacheLoader<String, Optional<NetworkInterfaceLoad>>() {
                @Throws(Exception::class)
                override fun load(s: String): Optional<NetworkInterfaceLoad> {
                    return networkMetrics.networkInterfaceLoadById(s)
                }
            })

    override fun connectivity(): Connectivity {
        return connectivityCache.get()
    }

    override fun networkInterfaces(): List<NetworkInterface> {
        return networkInterfacesCache.get()
    }

    override fun networkInterfaceById(id: String): Optional<NetworkInterface> {
        return networkInterfaceQueryCache.getUnchecked(id)
    }

    override fun networkInterfaceLoads(): List<NetworkInterfaceLoad> {
        return networkInterfaceLoadsCache.get()
    }

    override fun networkInterfaceLoadById(id: String): Optional<NetworkInterfaceLoad> {
        return networkInterfaceLoadQueryCache.getUnchecked(id)
    }
}