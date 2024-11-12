package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.metrics.CpuMetrics
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem
import reactor.core.publisher.Flux

@Component
class DefaultCpuMetrics(
    private val hal: HardwareAbstractionLayer,
    private val operatingSystem: OperatingSystem,
    @Qualifier("defaultCpuLoadMetrics") private val cpuLoadMetrics: DefaultCpuLoadMetrics
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
        return cpuLoadMetrics.createCpuLoad()
    }

    override fun cpuLoadEvents(): Flux<CpuLoad> {
        return cpuLoadMetrics.cpuLoadEvents()
    }

    override fun uptime(): Long {
        return operatingSystem.systemUptime
    }
}