package com.krillsson.sysapi.systemd

import java.time.Instant

data class SystemDaemonJournalEntry(
    val timestamp: Instant,
    val message: String
)