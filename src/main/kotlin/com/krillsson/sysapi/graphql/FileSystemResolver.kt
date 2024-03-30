package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.metrics.Metrics
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class FileSystemResolver(val metrics: Metrics) {
    @SchemaMapping(typeName="FileSystem", field="metrics")
    fun metrics(fileSystem: FileSystem) =
            metrics.fileSystemMetrics().fileSystemLoadById(fileSystem.id)
}