package com.krillsson.sysapi.docker

sealed class Status {
    object Available : Status()
    object Disabled : Status()
    data class Unavailable(val error: Throwable) : Status()
}