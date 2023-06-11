package com.krillsson.sysapi.config

data class LogReaderConfiguration(
    val files: Files = Files(),
    val journal: SystemDaemonJournal = SystemDaemonJournal()
) {
    data class Files(
        val paths: List<String> = emptyList()
    )
    data class SystemDaemonJournal(
        val services: List<String> = emptyList()
    )
}
