package com.krillsson.sysapi.windows.services

data class WindowsService(
    val name: String,
    val displayName: String,
    val serviceType: Type,
    val state: State,
    val pid: Int
) {

    enum class State(private val integerConstant: Int) {
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
    enum class Type(private val integerConstant: Int) {
        KERNEL_DRIVER(0x00000001),
        FILE_SYSTEM_DRIVER(0x00000002),
        ADAPTER(0x00000004),
        RECOGNIZER_DRIVER(0x00000008),
        WIN32_OWN_PROCESS(0x00000010),
        WIN32_SHARE_PROCESS(0x00000020),
        INTERACTIVE_PROCESS(0x00000100),
        UNKNOWN(Int.MAX_VALUE);

        companion object {
            fun fromIntegerConstant(constant: Int) =
                Type.values().firstOrNull { it.integerConstant == constant } ?: UNKNOWN
        }
    }
}
