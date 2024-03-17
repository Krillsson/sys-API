package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.disk.Disk
import com.krillsson.sysapi.core.metrics.Metrics
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class DiskResolver(val metrics: Metrics) : GraphQLResolver<Disk> {
    fun getId(drive: Disk) = drive.serial
    fun getMetrics(drive: Disk) = metrics.diskMetrics().diskLoadByName(drive.name)
}