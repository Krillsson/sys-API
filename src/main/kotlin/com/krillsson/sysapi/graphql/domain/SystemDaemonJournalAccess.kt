package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogRecord
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogSourceInfo
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsService
import com.krillsson.sysapi.systemd.SystemCtl
import com.krillsson.sysapi.systemd.SystemDaemonJournalEntry

interface SystemDaemonJournalAccess

interface SystemDaemonAccessAvailable : SystemDaemonJournalAccess {
    fun services(): List<SystemCtl.ListServicesOutput.Item>
    fun openJournal(name: String, limit: Int): List<SystemDaemonJournalEntry>

    fun openJournalConnection(
        name: String,
        after: String?,
        before: String?,
        first: Int?,
        last: Int?
    ): SystemDaemonJournalEntryConnection
    fun serviceDetails(name: String): SystemCtl.ServiceDetailsOutput?
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
    fun service(name: String): WindowsService?
}

data class WindowsManagementAccessUnavailable(
        val reason: String
) : WindowsManagementAccess