package com.krillsson.sysapi.core.domain.memory

data class MemoryInfo(
    val swapTotalBytes: Long,
    val totalBytes: Long,
    val physicalMemory: List<PhysicalMemory>
)