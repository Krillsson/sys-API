package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.memory.MemoryInfo
import com.krillsson.sysapi.core.metrics.Metrics
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class MemoryInfoResolver(val metrics: Metrics) : GraphQLResolver<MemoryInfo> {
    fun metrics(info: MemoryInfo) = metrics.memoryMetrics().memoryLoad()
}