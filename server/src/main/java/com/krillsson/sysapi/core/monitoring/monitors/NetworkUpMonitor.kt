package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toBooleanValue
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class NetworkUpMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.BooleanValue>
) : Monitor<MonitoredValue.BooleanValue>() {
    override val type: Type = Type.NETWORK_UP

    companion object {
        const val UP = 1.0
        const val DOWN = 0.0
    }

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.BooleanValue {
        return event.load().networkInterfaceLoads
            .stream()
            .filter { n: NetworkInterfaceLoad ->
                n.name.equals(
                    config.monitoredItemId,
                    ignoreCase = true
                ) || n.mac.equals(config.monitoredItemId, ignoreCase = true)
            }
            .map { n: NetworkInterfaceLoad -> n.isUp.toBooleanValue() }
            .findFirst()
            .orElse(false.toBooleanValue())
    }

    override fun isPastThreshold(value: MonitoredValue.BooleanValue): Boolean {
        return !value.value
    }
}