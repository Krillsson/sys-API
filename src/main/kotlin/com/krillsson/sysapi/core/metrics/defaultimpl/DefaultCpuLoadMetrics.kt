package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.cpu.CoreLoad
import com.krillsson.sysapi.util.round
import org.springframework.scheduling.annotation.Scheduled
import oshi.hardware.CentralProcessor
import oshi.hardware.CentralProcessor.TickType
import java.util.concurrent.TimeUnit

class DefaultCpuLoadMetrics(private val processor: CentralProcessor) {

    private var oldTicks: LongArray = LongArray(TickType.values().size)
    private var oldProcTicks: Array<LongArray> = Array(processor.logicalProcessorCount) {
        LongArray(
            TickType.values().size
        )
    }

    var coreLoads = updateCoreLoads()
    var systemUsage = updateSystemUsagePercent()

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    fun run() {
        coreLoads = updateCoreLoads()
        systemUsage = updateSystemUsagePercent()
    }

    private fun updateSystemUsagePercent(): Double {
        return (100.0 * processorTicks()).round(2)
    }

    private fun updateCoreLoads(): List<CoreLoad> {
        val ticks = coreTicks()
        return ticks.map {
            CoreLoad((100.0 * it).round(2))
        }
    }

    private fun processorTicks(): Double {
        val d = processor.getSystemCpuLoadBetweenTicks(oldTicks)
        oldTicks = processor.systemCpuLoadTicks
        return d
    }

    private fun coreTicks(): DoubleArray {
        val p = processor.getProcessorCpuLoadBetweenTicks(oldProcTicks)
        oldProcTicks = processor.processorCpuLoadTicks
        return p
    }
}