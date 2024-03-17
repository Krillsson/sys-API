package com.krillsson.sysapi.core.domain.gpu

class GpuLoad(
    val name: String,
    val coreLoad: Double,
    val memoryLoad: Double,
    val health: GpuHealth
) 