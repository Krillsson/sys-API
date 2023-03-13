package com.krillsson.sysapi.core.domain.drives

open class DrivePartition(
    val identification: String,
    val name: String,
    val type: String,
    val uuid: String,
    val sizeBytes: Long,
    val major: Int,
    val minor: Int,
    val mountPoint: String
) 