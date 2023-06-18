package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogReader
import com.krillsson.sysapi.systemd.Services
import com.krillsson.sysapi.systemd.SystemDaemonUnit

interface SystemDaemonJournalAccess

interface SystemDaemonAccessAvailable : SystemDaemonJournalAccess{
    fun services(): List<Services.ServicesItem>
    fun openJournal(name: String): List<SystemDaemonUnit.SystemDaemonJournalEntry>
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