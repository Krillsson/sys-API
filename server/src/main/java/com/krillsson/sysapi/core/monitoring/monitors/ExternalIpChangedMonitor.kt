package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.monitor.toBooleanValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class ExternalIpChangedMonitor(
    override val id: UUID,
    override val config: MonitorConfig<MonitoredValue.BooleanValue>
) : Monitor<MonitoredValue.BooleanValue>() {

    override val type: Type = Type.EXTERNAL_IP_CHANGED

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.BooleanValue {
        val externalIp = event.load.connectivity.externalIp
        val previousExternalIp = event.load.connectivity.previousExternalIp

        return if (externalIp == null || previousExternalIp == null) {
            true.toBooleanValue()
        } else if (externalIp != previousExternalIp) {
            false.toBooleanValue()
        } else {
            true.toBooleanValue()
        }
    }

    override fun isPastThreshold(value: MonitoredValue.BooleanValue): Boolean {
        return !value.value
    }
}