package com.krillsson.sysapi.systemd

sealed class CommandResult {
    object Success : CommandResult()
    object Unavailable : CommandResult()
    object Disabled : CommandResult()
    data class Failed(val error: Throwable) : CommandResult()
}