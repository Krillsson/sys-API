package com.krillsson.sysapi.logaccess.windowseventlog

enum class WindowsServiceState(private val integerConstant: Int) {
    STOPPED(0x00000001),
    START_PENDING(0x00000002),
    STOP_PENDING(0x00000003),
    RUNNING(0x00000004),
    CONTINUE_PENDING(0x00000005),
    PAUSE_PENDING(0x00000006),
    PAUSED(0x00000007),
    UNKNOWN(Int.MAX_VALUE);

    companion object {
        fun fromIntegerConstant(constant: Int) = values().firstOrNull { it.integerConstant == constant } ?: UNKNOWN
    }
}