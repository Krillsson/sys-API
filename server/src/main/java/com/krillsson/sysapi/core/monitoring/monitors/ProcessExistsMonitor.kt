package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toBooleanValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ProcessExistsMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.BooleanValue>
) : Monitor<MonitoredValue.BooleanValue>() {
    override val type: Type = Type.PROCESS_EXISTS

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.BooleanValue {
        val pid = config.monitoredItemId?.toInt()
        return event.load.processes.any { it.processID == pid }.toBooleanValue()
    }

    override fun isPastThreshold(value: MonitoredValue.BooleanValue): Boolean {
        return !value.value
    }
}