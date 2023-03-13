package com.krillsson.sysapi.core.domain.disk

data class Partition(
    val identification: String,
    val name: String,
    val type: String,
    val uuid: String,
    val sizeBytes: Long,
    val major: Int,
    val minor: Int,
    val mountPoint: String
)