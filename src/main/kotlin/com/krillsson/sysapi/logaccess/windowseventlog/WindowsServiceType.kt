package com.krillsson.sysapi.logaccess.windowseventlog

enum class WindowsServiceType(private val integerConstant: Int) {
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
            WindowsServiceType.values().firstOrNull { it.integerConstant == constant } ?: UNKNOWN
    }
}