package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.metrics.Metrics
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class FileSystemResolver(val metrics: Metrics) : GraphQLResolver<FileSystem> {
    fun getMetrics(fileSystem: com.krillsson.sysapi.core.domain.filesystem.FileSystem) =
        metrics.fileSystemMetrics().fileSystemLoadById(fileSystem.id)
}