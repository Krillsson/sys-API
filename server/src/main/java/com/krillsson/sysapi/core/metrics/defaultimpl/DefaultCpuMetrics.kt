package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Ticker.TickListener
import com.krillsson.sysapi.util.Utils
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

open class DefaultCpuMetrics(
    private val hal: HardwareAbstractionLayer,
    private val operatingSystem: OperatingSystem,
    private val cpuSensors: DefaultCpuSensors,
    private val cpuLoadMetrics: DefaultCpuLoadMetrics
) : CpuMetrics {

    fun register() {
        cpuLoadMetrics.register()
    }

    override fun cpuInfo(): CpuInfo {
        val processor = hal.processor
        val identifier = processor.processorIdentifier
        val centralProcessor = CentralProcessor(
            processor.logicalProcessorCount,
            processor.physicalProcessorCount,
            identifier.name,
            identifier.identifier,
            identifier.family,
            identifier.vendor,
            identifier.vendorFreq,
            identifier.model,
            identifier.stepping,
            identifier.isCpu64bit
        )
        return CpuInfo(centralProcessor)
    }

    override fun cpuLoad(): CpuLoad {
        val processor = hal.processor
        return CpuLoad(
            cpuLoadMetrics.systemUsage,
            Utils.round(processor.getSystemLoadAverage(1)[0] * 100.0, 2),
            cpuLoadMetrics.coreLoads,
            cpuSensors.cpuHealth(),
            operatingSystem.processCount,
            operatingSystem.threadCount
        )
    }

    override fun uptime(): Long {
        return operatingSystem.systemUptime
    }
}