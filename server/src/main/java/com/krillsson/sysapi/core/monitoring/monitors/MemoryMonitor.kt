package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class MemoryMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: Type = Type.MEMORY_SPACE

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        return event.load().memory.availableBytes.toDouble()
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value < config.threshold
    }
}