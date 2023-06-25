package com.krillsson.sysapi.logaccess.windowseventlog

data class WindowsService(
    val name: String,
    val displayName: String,
    val serviceType: WindowsServiceType,
    val state: WindowsServiceState,
    val pid: Int
)
