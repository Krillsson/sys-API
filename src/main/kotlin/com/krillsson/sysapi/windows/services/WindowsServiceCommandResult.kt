package com.krillsson.sysapi.windows.services

sealed class WindowsServiceCommandResult {
    object Success : WindowsServiceCommandResult()
    object Unavailable : WindowsServiceCommandResult()
    object Disabled : WindowsServiceCommandResult()
    data class Failed(val error: Throwable) : WindowsServiceCommandResult()
}