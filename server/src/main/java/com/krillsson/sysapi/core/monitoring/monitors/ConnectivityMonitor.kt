package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ConnectivityMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.ConditionalValue>
) : Monitor<MonitoredValue.ConditionalValue>() {

    companion object {
        val selector: ConditionalValueSelector = { load, _ ->
            load.connectivity.connected.toConditionalValue()
        }
    }

    override val type: Type = Type.CONNECTIVITY

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.ConditionalValue? =
        selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.ConditionalValue): Boolean {
        return !value.value
    }
}