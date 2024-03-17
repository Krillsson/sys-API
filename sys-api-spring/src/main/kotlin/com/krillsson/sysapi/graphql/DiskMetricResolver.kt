package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.disk.DiskLoad
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class DiskMetricResolver : GraphQLResolver<DiskLoad> {
    fun getDiskId(driveLoad: DiskLoad) = driveLoad.serial
    fun getReads(driveLoad: DiskLoad) = driveLoad.values.reads
    fun getWrites(driveLoad: DiskLoad) = driveLoad.values.writes
    fun getReadBytes(driveLoad: DiskLoad) = driveLoad.values.readBytes
    fun getWriteBytes(driveLoad: DiskLoad) = driveLoad.values.writeBytes
    fun getCurrentReadWriteRate(driveLoad: DiskLoad) =
        driveLoad.speed
}