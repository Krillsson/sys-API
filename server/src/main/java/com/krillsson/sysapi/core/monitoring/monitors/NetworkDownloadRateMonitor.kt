package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class NetworkDownloadRateMonitor(override val id: UUID, override val config: MonitorConfig<MonitoredValue.NumericalValue>) : Monitor<MonitoredValue.NumericalValue>() {
    override val type: Type = Type.NETWORK_DOWNLOAD_RATE

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.NumericalValue {
        return event.load().networkInterfaceLoads
            .stream()
            .filter { n: NetworkInterfaceLoad -> n.name.equals(config.monitoredItemId, ignoreCase = true) || n.mac.equals(config.monitoredItemId, ignoreCase = true) }
            .map { n: NetworkInterfaceLoad -> n.speed.receiveBytesPerSecond.toNumericalValue() }
            .findFirst()
            .orElse(MonitoredValue.NumericalValue(0))
    }

    override fun isPastThreshold(value: MonitoredValue.NumericalValue): Boolean {
        return value > config.threshold
    }
}