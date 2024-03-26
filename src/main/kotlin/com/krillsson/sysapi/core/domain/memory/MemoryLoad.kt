package com.krillsson.sysapi.core.domain.memory

class MemoryLoad(
    val numberOfProcesses: Int,
    val swapTotalBytes: Long,
    val swapUsedBytes: Long,
    val totalBytes: Long,
    val availableBytes: Long,
    val usedPercent: Double
) {
    val usedBytes = (totalBytes - availableBytes).coerceAtLeast(0)
}