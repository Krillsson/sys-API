package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.system.SystemLoad
import java.time.Duration
import java.util.*

interface Monitor {
    val id: UUID
    val type: MonitorType
    val config: Config

    fun selectValue(load: SystemLoad): Double
    fun isPastThreshold(value: Double): Boolean

    data class Config(
            val id: String? = null,
            val threshold: Double,
            val inertia: Duration
    )
}