package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.metrics.MemoryMetrics
import oshi.hardware.GlobalMemory
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

class DefaultMemoryMetrics(
    private val hal: HardwareAbstractionLayer,
    private val operatingSystem: OperatingSystem
) : MemoryMetrics {
    override fun memoryLoad(): MemoryLoad {
        val memory = hal.memory
        val virtualMemory = memory.virtualMemory
        return MemoryLoad(
            operatingSystem.processCount,
            virtualMemory.swapTotal,
            virtualMemory.swapUsed,
            memory.total,
            memory.available,
            usedPercent(memory).toDouble()
        )
    }

    private fun usedPercent(memory: GlobalMemory): Int {
        val free = memory.available
        val total = memory.total
        val used = total - free
        return (used * 100.0 / total + 0.5).toInt()
    }
}