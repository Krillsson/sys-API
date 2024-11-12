package com.krillsson.sysapi.core.metrics.cache

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.metrics.FileSystemMetrics
import reactor.core.publisher.Flux
import java.util.*

class CachingFileSystemMetrics(private val fileSystemMetrics: FileSystemMetrics, cacheConfiguration: CacheConfiguration) :
    FileSystemMetrics {
    private val fileSystemsCache: Supplier<List<FileSystem>> = Suppliers.memoizeWithExpiration(
        { fileSystemMetrics.fileSystems() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val fileSystemLoadsCache: Supplier<List<FileSystemLoad>> = Suppliers.memoizeWithExpiration(
        { fileSystemMetrics.fileSystemLoads() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )
    private val fileSystemQueryCache: LoadingCache<String, FileSystem?> = CacheBuilder.newBuilder()
        .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
        .build(object : CacheLoader<String, FileSystem>() {
            @Throws(Exception::class)
            override fun load(s: String): FileSystem? {
                return fileSystemMetrics.fileSystemById(s)
            }
        })
    private val fileSystemLoadsQueryCache: LoadingCache<String, FileSystemLoad?> =
        CacheBuilder.newBuilder()
            .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
            .build(object : CacheLoader<String, FileSystemLoad?>() {
                @Throws(Exception::class)
                override fun load(s: String): FileSystemLoad? {
                    return fileSystemMetrics.fileSystemLoadById(s)
                }
            })

    override fun fileSystems(): List<FileSystem> {
        return fileSystemsCache.get()
    }

    override fun fileSystemLoads(): List<FileSystemLoad> {
        return fileSystemLoadsCache.get()
    }

    override fun fileSystemById(id: String): FileSystem? {
        return fileSystemQueryCache.getUnchecked(id)
    }

    override fun fileSystemLoadById(id: String): FileSystemLoad? {
        return fileSystemLoadsQueryCache.getUnchecked(id)
    }

    override fun fileSystemEvents(): Flux<List<FileSystemLoad>> {
        return fileSystemMetrics.fileSystemEvents()
    }

    override fun fileSystemEventsById(id: String): Flux<FileSystemLoad> {
        return fileSystemMetrics.fileSystemEventsById(id)
    }
}