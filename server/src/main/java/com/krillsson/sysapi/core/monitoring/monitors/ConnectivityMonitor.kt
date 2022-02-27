package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ConnectivityMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.BooleanValue>
) : Monitor<MonitoredValue.BooleanValue>() {

    override val type: Type = Type.CONNECTIVITY

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.BooleanValue {
        return if (event.load.connectivity.connected) {
            MonitoredValue.BooleanValue(true)
        } else {
            MonitoredValue.BooleanValue(false)
        }
    }

    override fun isPastThreshold(value: MonitoredValue.BooleanValue): Boolean {
        return !value.value
    }
}