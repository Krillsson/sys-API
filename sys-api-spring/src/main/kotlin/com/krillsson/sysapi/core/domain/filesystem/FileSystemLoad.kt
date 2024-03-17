package com.krillsson.sysapi.core.domain.filesystem

data class FileSystemLoad(
    val name: String,
    val id: String,
    val freeSpaceBytes: Long,
    val usableSpaceBytes: Long,
    val totalSpaceBytes: Long
)