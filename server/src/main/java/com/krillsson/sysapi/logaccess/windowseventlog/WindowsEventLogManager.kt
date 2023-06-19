package com.krillsson.sysapi.logaccess.windowseventlog

import com.krillsson.sysapi.config.WindowsEventLogConfiguration
import com.krillsson.sysapi.graphql.domain.WindowsEventLogAccessAvailable
import oshi.PlatformEnum
import oshi.SystemInfo

class WindowsEventLogManager(private val config: WindowsEventLogConfiguration) : WindowsEventLogAccessAvailable {
    fun supportedBySystem(): Boolean {
        return SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWS || SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWSCE
    }
    override fun openEventLog(name: String) = eventLogs().firstOrNull { it.name == name }?.readAll().orEmpty()
    override fun eventLogs() = config.names.map { WindowsEventLogReader(it) }
}
