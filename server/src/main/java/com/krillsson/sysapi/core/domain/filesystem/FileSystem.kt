package com.krillsson.sysapi.core.domain.filesystem

data class FileSystem(
    val name: String,
    val description: String,
    val label: String,
    val type: String,
    val volume: String,
    val mount: String
)