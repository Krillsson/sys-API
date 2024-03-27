package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.cpu.LoadAverages
import com.krillsson.sysapi.core.metrics.CpuMetrics
import com.krillsson.sysapi.util.round
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

open class DefaultCpuMetrics(
    private val hal: HardwareAbstractionLayer,
    private val operatingSystem: OperatingSystem,
    private val cpuSensors: DefaultCpuSensors,
    private val cpuLoadMetrics: DefaultCpuLoadMetrics
) : CpuMetrics {

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
        val systemLoadAverage = processor.getSystemLoadAverage(3)
        val loadAverages = LoadAverages(systemLoadAverage[0], systemLoadAverage[1], systemLoadAverage[2])
        return CpuLoad(
            cpuLoadMetrics.systemUsage,
            (systemLoadAverage[0] * 100.0).round(2),
            loadAverages,
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