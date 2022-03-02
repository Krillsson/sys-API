package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ExternalIpChangedMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.ConditionalValue>
) : Monitor<MonitoredValue.ConditionalValue>() {

    companion object {
        val selector: ConditionalValueSelector = { load, _ ->
            val externalIp = load.connectivity.externalIp
            val previousExternalIp = load.connectivity.previousExternalIp

            if (externalIp == null || previousExternalIp == null) {
                true.toConditionalValue()
            } else if (externalIp != previousExternalIp) {
                false.toConditionalValue()
            } else {
                true.toConditionalValue()
            }
        }
    }

    override val type: Type = Type.EXTERNAL_IP_CHANGED

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.ConditionalValue? = selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.ConditionalValue): Boolean {
        return !value.value
    }
}