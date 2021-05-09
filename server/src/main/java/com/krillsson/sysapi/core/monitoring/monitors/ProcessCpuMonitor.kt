package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class ProcessCpuMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: MonitorType = MonitorType.PROCESS_CPU_LOAD

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        val pid = config.monitoredItemId?.toInt()
        return event.load.processes.firstOrNull { it.processID == pid }?.cpuPercent ?: -1.0
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value < config.threshold
    }
}