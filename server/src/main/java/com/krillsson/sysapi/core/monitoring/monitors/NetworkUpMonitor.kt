package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class NetworkUpMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: MonitorType = MonitorType.NETWORK_UP

    companion object {
        const val UP = 1.0
        const val DOWN = 0.0
    }

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        return event.load().networkInterfaceLoads
            .stream()
            .filter { n: NetworkInterfaceLoad -> n.name.equals(config.monitoredItemId, ignoreCase = true) }
            .map { n: NetworkInterfaceLoad -> if (n.isUp) UP else DOWN }
            .findFirst()
            .orElse(DOWN)
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value != UP
    }
}