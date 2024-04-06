package com.krillsson.sysapi.core.speed

import java.time.LocalDateTime

data class SpeedMeasurement(
        val read: Long,
        val write: Long,
        val sampledAt: LocalDateTime
)
