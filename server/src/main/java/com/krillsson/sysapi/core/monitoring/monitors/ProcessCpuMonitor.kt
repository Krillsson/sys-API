package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toFractionalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ProcessCpuMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.FractionalValue>) : Monitor<MonitoredValue.FractionalValue>() {
    override val type: Type = Type.PROCESS_CPU_LOAD

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.FractionalValue {
        val pid = config.monitoredItemId?.toInt()
        return event.load.processes.firstOrNull { it.processID == pid }?.cpuPercent?.toFractionalValue() ?: 0.0.toFractionalValue()
    }

    override fun isPastThreshold(value: MonitoredValue.FractionalValue): Boolean {
        return value < config.threshold
    }
}