package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.MonitorInput
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.Duration
import java.util.*

class NetworkUpMonitor(override val id: UUID, private val nicId: String, override val inertia: Duration, override val threshold: Double) : MonitorInput {
    override val type: MonitorType = MonitorType.NETWORK_UP

    override fun value(systemLoad: SystemLoad): Double {
        return systemLoad.networkInterfaceLoads
                .stream()
                .filter { n: NetworkInterfaceLoad -> n.name.equals(nicId, ignoreCase = true) }
                .map { n: NetworkInterfaceLoad -> if (n.isUp) 1.0 else 0.0 }
                .findFirst()
                .orElse(0.0)
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value != 0.0
    }
}