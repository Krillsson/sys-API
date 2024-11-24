package com.krillsson.sysapi.logaccess

import java.time.Instant

data class LogMessage(
    val message: String,
    val level: Level?,
    val timestamp: Instant?
) {
    enum class Level {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL, UNKNOWN
    }
}
