package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.memory.MemoryInfo
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import reactor.core.publisher.Flux

interface MemoryMetrics {
    fun memoryLoad(): MemoryLoad

    fun memoryLoadEvents(): Flux<MemoryLoad>
    fun memoryInfo(): MemoryInfo
}