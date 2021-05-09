package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class CpuMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {

    override val type: MonitorType = MonitorType.CPU_LOAD

    override fun selectValue(event: MonitorMetricQueryEvent): Double = event.load().cpuLoad.usagePercentage

    override fun isPastThreshold(value: Double): Boolean {
        return value > config.threshold
    }
}