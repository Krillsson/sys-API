package com.krillsson.sysapi.core.domain.disk

data class Disk(
    val model: String,
    val name: String,
    val serial: String,
    val sizeBytes: Long,
    val partitions: List<Partition>
)