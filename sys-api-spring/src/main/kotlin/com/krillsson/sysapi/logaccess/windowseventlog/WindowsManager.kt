package com.krillsson.sysapi.logaccess.windowseventlog

import com.krillsson.sysapi.config.WindowsConfiguration
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.graphql.domain.WindowsManagementAccessAvailable
import org.springframework.stereotype.Service
import oshi.PlatformEnum
import oshi.SystemInfo

@Service
class WindowsManager(config: YAMLConfigFile) : WindowsManagementAccessAvailable {
    fun supportedBySystem(): Boolean {
        return SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWS || SystemInfo.getCurrentPlatform() == PlatformEnum.WINDOWSCE
    }

    private val reader = WindowsEventLogReader(config.windows.eventLog)
    private val serviceManager = WindowsServiceManager(config.windows.serviceManagement)
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

    override fun service(name: String): WindowsService? {
        return serviceManager.service(name)
    }

    fun performWindowsServiceCommand(serviceName: String, command: WindowsServiceCommand): WindowsServiceCommandResult {
        return serviceManager.performWindowsServiceCommand(serviceName, command)
    }
}
