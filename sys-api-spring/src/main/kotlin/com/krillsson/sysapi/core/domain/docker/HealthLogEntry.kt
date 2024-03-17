package com.krillsson.sysapi.core.domain.docker

data class HealthLogEntry(
    val start: String,
    val end: String,
    val output: String,
    val exitCode: Long
)