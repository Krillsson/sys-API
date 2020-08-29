package com.krillsson.sysapi.core.domain.drives

open class Partition(
    val identification: String,
    val name: String,
    val type: String,
    val uuid: String,
    val size: Long,
    val major: Int,
    val minor: Int,
    val mountPoint: String
) 