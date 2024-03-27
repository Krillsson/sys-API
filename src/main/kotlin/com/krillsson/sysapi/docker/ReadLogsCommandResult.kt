package com.krillsson.sysapi.docker

sealed class ReadLogsCommandResult {
    data class Success(val lines: List<String>) : ReadLogsCommandResult()
    object Unavailable : ReadLogsCommandResult()
    data class Failed(val error: RuntimeException) : ReadLogsCommandResult()
    data class TimedOut(val timeoutSeconds: Long) : ReadLogsCommandResult()
}