package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.disk.DiskLoad
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "DiskMetrics")
class DiskMetricResolver {
    @SchemaMapping
    fun id(driveLoad: DiskLoad) = driveLoad.name

    @SchemaMapping
    fun reads(driveLoad: DiskLoad) = driveLoad.values.reads

    @SchemaMapping
    fun writes(driveLoad: DiskLoad) = driveLoad.values.writes

    @SchemaMapping
    fun readBytes(driveLoad: DiskLoad) = driveLoad.values.readBytes

    @SchemaMapping
    fun writeBytes(driveLoad: DiskLoad) = driveLoad.values.writeBytes

    @SchemaMapping
    fun currentReadWriteRate(driveLoad: DiskLoad) =
            driveLoad.speed
}