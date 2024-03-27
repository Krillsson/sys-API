package com.krillsson.sysapi.core.domain.docker

data class Health(
    val status: String,
    val failingStreak: Int,
    val logEntries: List<HealthLogEntry>
)