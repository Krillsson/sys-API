package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.system.SystemLoad
import java.util.UUID

abstract class Monitor {
    abstract val id: UUID
    abstract val type: MonitorType
    abstract val config: MonitorConfig

    abstract fun selectValue(load: SystemLoad): Double
    abstract fun isPastThreshold(value: Double): Boolean

    fun check(load: SystemLoad): Boolean = isPastThreshold(selectValue(load))
}