package com.krillsson.sysapi.rest

import com.krillsson.sysapi.dto.monitor.MonitorType

data class CreateMonitor(
    var idToMonitor: String,
    var inertiaInSeconds: Long,
    var type: MonitorType,
    var threshold: Double
)