package com.krillsson.sysapi.core.domain.monitor

import java.time.Duration

data class MonitorConfig<T : MonitoredValue>(
    val monitoredItemId: String? = null,
    val threshold: T,
    val inertia: Duration
)