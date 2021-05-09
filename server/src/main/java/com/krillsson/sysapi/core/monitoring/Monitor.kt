package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import java.util.*

abstract class Monitor {
    abstract val id: UUID
    abstract val type: MonitorType
    abstract val config: MonitorConfig

    abstract fun selectValue(event: MonitorMetricQueryEvent): Double
    abstract fun isPastThreshold(value: Double): Boolean

    fun check(event: MonitorMetricQueryEvent): Boolean = isPastThreshold(selectValue(event))
}