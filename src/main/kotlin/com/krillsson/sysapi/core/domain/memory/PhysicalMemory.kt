package com.krillsson.sysapi.core.domain.memory

data class PhysicalMemory(
    val bankLabel: String,
    val capacityBytes: Long,
    val clockSpeedHertz: Long,
    val manufacturer: String,
    val memoryType: String
)