package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogRecord
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogSourceInfo
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsService
import com.krillsson.sysapi.systemd.SystemCtlServicesOutput
import com.krillsson.sysapi.systemd.SystemDaemonJournalEntry

interface SystemDaemonJournalAccess

interface SystemDaemonAccessAvailable : SystemDaemonJournalAccess {
    fun services(): List<SystemCtlServicesOutput.Item>
    fun openJournal(name: String): List<SystemDaemonJournalEntry>
}

data class SystemDaemonAccessUnavailable(
    val reason: String
) : SystemDaemonJournalAccess

interface WindowsManagementAccess

interface WindowsManagementAccessAvailable : WindowsManagementAccess {
    fun openEventLogBySource(source: String): List<WindowsEventLogRecord>
    fun openApplicationEventLog(): List<WindowsEventLogRecord>
    fun openSystemEventLog(): List<WindowsEventLogRecord>
    fun openSecurityEventLog(): List<WindowsEventLogRecord>
    fun eventLogs(): List<WindowsEventLogSourceInfo>
    fun services(): List<WindowsService>
}

data class WindowsManagementAccessUnavailable(
    val reason: String
) : WindowsManagementAccess