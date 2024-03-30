package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.memory.MemoryInfo
import com.krillsson.sysapi.core.metrics.Metrics
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "Memory")
class MemoryInfoResolver(val metrics: Metrics) {
    @SchemaMapping
    fun metrics(info: MemoryInfo) = metrics.memoryMetrics().memoryLoad()
}