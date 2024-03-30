package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName="ProcessorMetrics")
class ProcessorMetricsResolver {
    @SchemaMapping
    fun voltage(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.voltage.toInt()
    @SchemaMapping
    fun fanRpm(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.fanRpm.toInt()
    @SchemaMapping
    fun fanPercent(cpuLoad: CpuLoad) =
            cpuLoad.cpuHealth.fanPercent.toInt()

    @SchemaMapping
    fun temperatures(cpuLoad: CpuLoad) =
            cpuLoad.cpuHealth.temperatures
}