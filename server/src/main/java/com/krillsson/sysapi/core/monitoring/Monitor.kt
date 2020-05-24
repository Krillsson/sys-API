package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.system.SystemLoad
import java.time.Duration
import java.util.*

abstract class Monitor {
    abstract val id: UUID
    abstract val type: MonitorType
    abstract val config: Config

    abstract fun selectValue(load: SystemLoad): Double
    abstract fun isPastThreshold(value: Double): Boolean

    fun failure(load: SystemLoad): Boolean = isPastThreshold(selectValue(load))

    data class Config(
            val id: String? = null,
            val threshold: Double,
            val inertia: Duration
    )
}