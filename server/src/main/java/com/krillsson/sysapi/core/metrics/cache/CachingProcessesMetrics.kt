package com.krillsson.sysapi.core.metrics.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.processes.ProcessesInfo
import com.krillsson.sysapi.core.metrics.ProcessesMetrics
import java.util.Optional
import java.util.concurrent.ExecutionException

/**
 * Using the process sort and limit arguments as the key for the ProcessInfo cache will cause
 * a real call for when one tiny thing in arguments changes. To prevent a huge cache we set max size of this cache to 1.
 * It might turn out that we don't want to cache processes at all because of the size in RAM.
 */
class CachingProcessesMetrics(
    private val processesMetrics: ProcessesMetrics,
    cacheConfiguration: CacheConfiguration
) : ProcessesMetrics {
    private val processQueryCache: LoadingCache<Int, Optional<Process>> = CacheBuilder.newBuilder()
        .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
        .build(object : CacheLoader<Int?, Optional<Process>>() {
            @Throws(Exception::class)
            override fun load(s: Int?): Optional<Process> {
                return processesMetrics.getProcessByPid(s!!)
            }
        })
    private val processesInfoCache: Cache<String, ProcessesInfo> = CacheBuilder.newBuilder()
        .maximumSize(1)
        .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
        .build()

    override fun getProcessByPid(pid: Int): Optional<Process> {
        return processQueryCache.getUnchecked(pid)
    }

    override fun processesInfo(sortBy: ProcessSort, limit: Int): ProcessesInfo {
        return try {
            processesInfoCache[sortBy.name + ":" + limit.toString(), {
                processesMetrics.processesInfo(
                    sortBy,
                    limit
                )
            }]
        } catch (e: ExecutionException) {
            throw IllegalStateException(e)
        }
    }
}