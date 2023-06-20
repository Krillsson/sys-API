package com.krillsson.sysapi.logaccess.windowseventlog

import com.krillsson.sysapi.config.WindowsEventLogConfiguration
import com.krillsson.sysapi.graphql.domain.WindowsEventLogAccessAvailable
import oshi.PlatformEnum
import oshi.SystemInfo

class WindowsEventLogManager(private val config: WindowsEventLogConfiguration) : WindowsEventLogAccessAvailable {
    fun supportedBySystem(): Boolean {
        return SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWS || SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWSCE
    }

    private val reader = WindowsEventLogReader()
    override fun openEventLogBySource(source: String) = reader.readAllBySource(source)
    override fun openApplicationEventLog(): List<WindowsEventLogRecord> {
        return reader.readAllApplication()
    }

    override fun openSystemEventLog(): List<WindowsEventLogRecord> {
        return reader.readAllSystem()
    }

    override fun openSecurityEventLog(): List<WindowsEventLogRecord> {
        return reader.readAllSecurity()
    }

    override fun eventLogs() = reader.allSources()
}
