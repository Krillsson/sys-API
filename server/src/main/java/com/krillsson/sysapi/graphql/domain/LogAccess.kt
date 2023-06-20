package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogRecord
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogSourceInfo
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

interface WindowsEventLogAccess

interface WindowsEventLogAccessAvailable : WindowsEventLogAccess {
    fun openEventLogBySource(source: String): List<WindowsEventLogRecord>
    fun openApplicationEventLog(): List<WindowsEventLogRecord>
    fun openSystemEventLog(): List<WindowsEventLogRecord>
    fun openSecurityEventLog(): List<WindowsEventLogRecord>
    fun eventLogs(): List<WindowsEventLogSourceInfo>
}

data class WindowsEventLogAccessUnavailable(
    val reason: String
) : WindowsEventLogAccess