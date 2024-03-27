package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.metrics.Metrics
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class ProcessorResolver(val metrics: Metrics) : GraphQLResolver<CentralProcessor> {
    fun getMetrics(processor: CentralProcessor) = metrics.cpuMetrics().cpuLoad()
}