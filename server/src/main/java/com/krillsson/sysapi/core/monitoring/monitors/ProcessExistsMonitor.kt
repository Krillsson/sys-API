package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ProcessExistsMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: Type = Type.PROCESS_EXISTS

    companion object {
        const val UP = 1.0
        const val DOWN = 0.0
    }

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        val pid = config.monitoredItemId?.toInt()
        return if (event.load.processes.any { it.processID == pid }) UP else DOWN
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value != UP
    }
}