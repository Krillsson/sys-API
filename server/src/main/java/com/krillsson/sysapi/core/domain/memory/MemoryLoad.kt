package com.krillsson.sysapi.core.domain.memory

class MemoryLoad(
    val numberOfProcesses: Int,
    val swapTotal: Long,
    val swapUsed: Long,
    val total: Long,
    val available: Long
) 