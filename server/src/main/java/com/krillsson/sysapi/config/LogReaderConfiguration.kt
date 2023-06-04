package com.krillsson.sysapi.config

data class LogReaderConfiguration(
    val files: Files = Files()
) {
    data class Files(
        val paths: List<String> = emptyList()
    )
}
