package com.krillsson.sysapi.core.domain.drives

class OsPartition(
    identification: String,
    name: String,
    type: String,
    uuid: String,
    sizeBytes: Long,
    major: Int,
    minor: Int,
    mountPoint: String,
    val volume: String,
    val logicalVolume: String,
    val mount: String,
    val description: String,
    val usableSpace: Long,
    val totalSpace: Long
) : Partition(
    identification,
    name,
    type,
    uuid,
    sizeBytes,
    major,
    minor,
    mountPoint
) 