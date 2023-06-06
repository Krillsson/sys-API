package com.krillsson.sysapi.graphql.domain

interface SystemDaemonJournalAccess

object SystemDaemonJournalAccessAvailable : SystemDaemonJournalAccess

data class SystemDaemonJournalAccessUnavailable(
    val reason: String
) : SystemDaemonJournalAccess

interface WindowsEventLogAccess

object WindowsEventLogAccessAvailable : WindowsEventLogAccess

data class WindowsEventLogAccessUnavailable(
    val reason: String
) : SystemDaemonJournalAccess