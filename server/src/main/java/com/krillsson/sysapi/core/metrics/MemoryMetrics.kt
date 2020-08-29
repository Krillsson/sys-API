package com.krillsson.sysapi.core.metrics

import com.krillsson.sysapi.core.domain.memory.MemoryLoad

interface MemoryMetrics {
    fun memoryLoad(): MemoryLoad
}