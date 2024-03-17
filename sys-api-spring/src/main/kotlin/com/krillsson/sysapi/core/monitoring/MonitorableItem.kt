package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitoredValue

data class MonitorableItem(
    val id: String?,
    val name: String,
    val description: String?,
    val maxValue: MonitoredValue,
    val currentValue: MonitoredValue,
    val type: Monitor.Type
)