package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class NetworkUpMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.ConditionalValue>
) : Monitor<MonitoredValue.ConditionalValue>() {
    override val type: Type = Type.NETWORK_UP

    companion object {
        val selector: ConditionalValueSelector = { load, monitoredItemId ->
            load.networkInterfaceLoads.firstOrNull { n: NetworkInterfaceLoad ->
                n.name.equals(monitoredItemId, ignoreCase = true) || n.mac.equals(monitoredItemId, ignoreCase = true)
            }?.isUp?.toConditionalValue()
        }
    }

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.ConditionalValue? {
        return selector(event.load, config.monitoredItemId)
    }

    override fun isPastThreshold(value: MonitoredValue.ConditionalValue): Boolean {
        return !value.value
    }
}