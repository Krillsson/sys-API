package com.krillsson.sysapi.core.domain.cpu

data class LoadAverages(
    val oneMinute: Double,
    val fiveMinutes: Double,
    val fifteenMinutes: Double
)