package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class NetworkUploadRateMonitor(override val id: UUID, override val config: MonitorConfig) : Monitor() {
    override val type: MonitorType = MonitorType.NETWORK_UPLOAD_RATE

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        return event.load().networkInterfaceLoads
            .stream()
            .filter { n: NetworkInterfaceLoad -> n.name.equals(config.monitoredItemId, ignoreCase = true) || n.mac.equals(config.monitoredItemId, ignoreCase = true) }
            .map { n: NetworkInterfaceLoad -> n.speed.sendBytesPerSecond.toDouble() }
            .findFirst()
            .orElse(0.0)
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value > config.threshold
    }
}