package com.krillsson.sysapi.config

data class LogReaderConfiguration(
        val files: List<String> = emptyList(),
        val directories: List<String> = emptyList()
)
