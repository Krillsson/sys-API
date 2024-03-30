package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.metrics.Metrics
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName="Processor")
class ProcessorResolver(val metrics: Metrics) {
    @SchemaMapping
    fun metrics(processor: CentralProcessor) = metrics.cpuMetrics().cpuLoad()
}