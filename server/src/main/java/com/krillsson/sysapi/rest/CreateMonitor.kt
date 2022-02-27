package com.krillsson.sysapi.rest

import com.krillsson.sysapi.core.monitoring.Monitor

data class CreateMonitor(
    var idToMonitor: String,
    var inertiaInSeconds: Long,
    var type: Monitor.Type,
    var threshold: Double
)