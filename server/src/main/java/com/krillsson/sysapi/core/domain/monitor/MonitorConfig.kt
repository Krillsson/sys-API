package com.krillsson.sysapi.core.domain.monitor

import java.time.Duration

data class MonitorConfig(
            val id: String? = null,
            val threshold: Double,
            val inertia: Duration
    )