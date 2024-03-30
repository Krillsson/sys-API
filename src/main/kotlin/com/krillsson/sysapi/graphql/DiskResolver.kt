package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.disk.Disk
import com.krillsson.sysapi.core.metrics.Metrics
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "Disk")
class DiskResolver(val metrics: Metrics) {
    @SchemaMapping
    fun id(drive: Disk) = drive.name
    @SchemaMapping
    fun metrics(drive: Disk) = metrics.diskMetrics().diskLoadByName(drive.name)
}