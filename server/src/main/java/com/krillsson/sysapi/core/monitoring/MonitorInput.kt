package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.system.SystemLoad
import java.time.Duration
import java.util.*

interface MonitorInput {
    fun value(systemLoad: SystemLoad): Double
    fun isPastThreshold(value: Double): Boolean
    val threshold: Double
    val id: UUID
    val inertia: Duration
    val type: MonitorType
}