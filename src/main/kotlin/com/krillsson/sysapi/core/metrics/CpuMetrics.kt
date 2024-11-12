package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.cpu.CpuInfo
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import reactor.core.publisher.Flux

interface CpuMetrics {
    fun cpuInfo(): CpuInfo
    fun cpuLoad(): CpuLoad
    fun cpuLoadEvents(): Flux<CpuLoad>
    fun uptime(): Long
}