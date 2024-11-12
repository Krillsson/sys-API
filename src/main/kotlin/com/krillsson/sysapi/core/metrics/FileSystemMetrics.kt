package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import reactor.core.publisher.Flux

interface FileSystemMetrics {
    fun fileSystems(): List<FileSystem>
    fun fileSystemById(id: String): FileSystem?
    fun fileSystemLoads(): List<FileSystemLoad>
    fun fileSystemLoadById(id: String): FileSystemLoad?

    fun fileSystemEvents(): Flux<List<FileSystemLoad>>

    fun fileSystemEventsById(id: String): Flux<FileSystemLoad>
}