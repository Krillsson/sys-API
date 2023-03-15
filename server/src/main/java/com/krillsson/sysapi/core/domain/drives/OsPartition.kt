package com.krillsson.sysapi.core.domain.drives

class OsPartition(
    val identification: String,
    val name: String,
    val type: String,
    val uuid: String,
    val sizeBytes: Long,
    val major: Int,
    val minor: Int,
    val mountPoint: String,
    val volume: String,
    val logicalVolume: String,
    val mount: String,
    val description: String,
    val usableSpace: Long,
    val totalSpace: Long
)