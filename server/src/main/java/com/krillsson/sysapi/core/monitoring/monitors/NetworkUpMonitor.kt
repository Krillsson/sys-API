package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class NetworkUpMonitor(override val id: UUID, override val config: Monitor.Config) : Monitor {
    override val type: MonitorType = MonitorType.NETWORK_UP

    override fun selectValue(load: SystemLoad): Double {
        return load.networkInterfaceLoads
                .stream()
                .filter { n: NetworkInterfaceLoad -> n.name.equals(config.id, ignoreCase = true) }
                .map { n: NetworkInterfaceLoad -> if (n.isUp) 1.0 else 0.0 }
                .findFirst()
                .orElse(0.0)
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value != 0.0
    }
}