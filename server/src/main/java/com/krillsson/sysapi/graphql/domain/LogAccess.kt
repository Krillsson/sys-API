package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.logaccess.systemd.SystemDaemonJournalReader
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogReader


object LogAccess
interface SystemDaemonJournalAccess

interface SystemDaemonJournalAccessAvailable : SystemDaemonJournalAccess{
    fun journals(): List<SystemDaemonJournalReader>
    fun openSystemDaemonJournal(name: String): List<String>
}

data class SystemDaemonJournalAccessUnavailable(
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