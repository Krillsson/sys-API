package com.krillsson.sysapi.core.domain.drives

class DriveValues(
    val usableSpace: Long,
    val totalSpace: Long,
    val openFileDescriptors: Long,
    val maxFileDescriptors: Long,
    val reads: Long,
    val readBytes: Long,
    val writes: Long,
    val writeBytes: Long
) 