package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogReader
import com.krillsson.sysapi.systemd.SystemCtlServicesOutput
import com.krillsson.sysapi.systemd.SystemDaemonJournalEntry

interface SystemDaemonJournalAccess

interface SystemDaemonAccessAvailable : SystemDaemonJournalAccess{
    fun services(): List<SystemCtlServicesOutput.Item>
    fun openJournal(name: String): List<SystemDaemonJournalEntry>
}

data class SystemDaemonAccessUnavailable(
    val reason: String
) : SystemDaemonJournalAccess

interface WindowsEventLogAccess

interface WindowsEventLogAccessAvailable : WindowsEventLogAccess {
    fun openEventLog(name: String): List<String>
    fun eventLogs(): List<WindowsEventLogReader>
}

data class WindowsEventLogAccessUnavailable(
    val reason: String
) : WindowsEventLogAccess