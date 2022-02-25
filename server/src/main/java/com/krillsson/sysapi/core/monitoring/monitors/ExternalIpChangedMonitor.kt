package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class ExternalIpChangedMonitor(
    override val id: UUID,
    override val config: MonitorConfig
) : Monitor() {

    override val type: MonitorType = MonitorType.EXTERNAL_IP_CHANGED

    override fun selectValue(event: MonitorMetricQueryEvent): Double {
        val externalIp = event.load.connectivity.externalIp
        val previousExternalIp = event.load.connectivity.previousExternalIp

        return if (externalIp == null || previousExternalIp == null) {
            1.0
        } else if (externalIp != previousExternalIp) {
            0.0
        } else {
            1.0
        }
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value == 0.0
    }
}