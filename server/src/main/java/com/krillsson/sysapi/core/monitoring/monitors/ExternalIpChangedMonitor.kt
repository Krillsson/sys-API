package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.monitoring.MetricQueryEvent
import com.krillsson.sysapi.core.monitoring.Monitor
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

    override fun selectValue(event: MetricQueryEvent): MonitoredValue.ConditionalValue? = selector(event.load, null)
    override fun maxValue(info: SystemInfo): MonitoredValue.ConditionalValue? {
        return MonitoredValue.ConditionalValue(true)
    }

    override fun isPastThreshold(value: MonitoredValue.ConditionalValue): Boolean {
        return !value.value
    }
}