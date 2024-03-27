package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class ProcessorMetricsResolver : GraphQLResolver<CpuLoad> {
    fun getVoltage(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.voltage.toInt()
    fun getFanRpm(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.fanRpm.toInt()
    fun getFanPercent(cpuLoad: CpuLoad) =
        cpuLoad.cpuHealth.fanPercent.toInt()

    fun getTemperatures(cpuLoad: CpuLoad) =
        cpuLoad.cpuHealth.temperatures
}