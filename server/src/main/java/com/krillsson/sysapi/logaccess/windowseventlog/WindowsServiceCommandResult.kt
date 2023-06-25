package com.krillsson.sysapi.logaccess.windowseventlog

sealed class WindowsServiceCommandResult {
    object Success : WindowsServiceCommandResult()
    object Unavailable : WindowsServiceCommandResult()
    data class Failed(val error: Throwable) : WindowsServiceCommandResult()
}