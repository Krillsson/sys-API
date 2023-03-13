package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.cpu.CoreLoad
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Ticker.TickListener
import com.krillsson.sysapi.util.Utils
import com.krillsson.sysapi.util.logger
import oshi.hardware.CentralProcessor
import oshi.hardware.CentralProcessor.TickType

class DefaultCpuLoadMetrics(private val processor: CentralProcessor, private val ticker: Ticker) : TickListener {

    private val logger by logger()

    private var oldTicks: LongArray = LongArray(TickType.values().size)
    private var oldProcTicks: Array<LongArray> = Array(processor.logicalProcessorCount) {
        LongArray(
            TickType.values().size
        )
    }

    var coreLoads = updateCoreLoads()
    var systemUsage = updateSystemUsagePercent()

    fun register() {
        ticker.register(this)
    }

    override fun onTick() {
        coreLoads = updateCoreLoads()
        systemUsage = updateSystemUsagePercent()
        logger.info("New CPU load percent $systemUsage")
    }

    private fun updateSystemUsagePercent(): Double {
        return Utils.round(100.0 * processorTicks(), 2)
    }

    private fun updateCoreLoads(): List<CoreLoad> {
        val ticks = coreTicks()
        return ticks.map {
            CoreLoad(Utils.round(100.0 * it, 2))
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