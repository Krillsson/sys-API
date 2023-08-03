package com.krillsson.sysapi.logaccess.windowseventlog

import java.time.Instant

data class WindowsEventLogRecord(
    val timestamp: Instant,
    val message: String,
    val eventType: Type,
    val source: String,
    val category: String
) {
    enum class Type {
        Error, Warning, Informational, AuditSuccess, AuditFailure
    }
}
