package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ConnectivityMonitor(
    override val id: UUID,
    override val config: MonitorConfig
) : Monitor() {

    override val type: Monitor.Type = Type.CONNECTIVITY

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        return if (event.load.connectivity.connected) {
            1.0
        } else {
            0.0
        }
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value == 0.0
    }
}