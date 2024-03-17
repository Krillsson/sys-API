package com.krillsson.sysapi.docker

sealed class CommandResult {
    object Success : CommandResult()
    object Unavailable : CommandResult()
    data class Failed(val error: RuntimeException) : CommandResult()
}