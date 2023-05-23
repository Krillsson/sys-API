package com.krillsson.sysapi.core.domain.cpu

data class LoadAverages(
    val oneMinutes: Double,
    val fiveMinutes: Double,
    val fifteenMinutes: Double
)