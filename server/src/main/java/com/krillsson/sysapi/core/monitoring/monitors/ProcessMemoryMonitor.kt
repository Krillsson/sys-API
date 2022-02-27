package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ProcessMemoryMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: Type = Type.PROCESS_MEMORY_SPACE

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        val pid = config.monitoredItemId?.toInt()
        return event.load.processes.firstOrNull { it.processID == pid }?.residentSetSize?.toDouble() ?: -1.0
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value < config.threshold
    }
}