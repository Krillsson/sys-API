package com.krillsson.sysapi.core.domain.disk

data class DiskValues(
    val reads: Long,
    val readBytes: Long,
    val writes: Long,
    val writeBytes: Long
)