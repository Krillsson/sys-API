package com.krillsson.sysapi.logaccess.windowseventlog

import com.krillsson.sysapi.config.WindowsEventLogConfiguration
import com.krillsson.sysapi.graphql.domain.WindowsManagementAccessAvailable
import oshi.PlatformEnum
import oshi.SystemInfo

class WindowsManager(private val config: WindowsEventLogConfiguration) : WindowsManagementAccessAvailable {
    fun supportedBySystem(): Boolean {
        return SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWS || SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWSCE
    }

    private val reader = WindowsEventLogReader()

    private val serviceManager = WindowsServiceManager()
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
    override fun services(): List<WindowsService> {
        return serviceManager.services()
    }

    fun performWindowsServiceCommand(serviceName: String, command: WindowsServiceCommand): WindowsServiceCommandResult {
        return serviceManager.performWindowsServiceCommand(serviceName, command)
    }
}
