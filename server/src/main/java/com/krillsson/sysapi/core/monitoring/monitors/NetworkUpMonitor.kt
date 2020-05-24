package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class NetworkUpMonitor(override val id: UUID, override val config: Config) : Monitor() {
    override val type: MonitorType = MonitorType.NETWORK_UP

    companion object {
        const val UP = 1.0
        const val DOWN = 0.0
    }

    override fun selectValue(load: SystemLoad): Double {
        return load.networkInterfaceLoads
                .stream()
                .filter { n: NetworkInterfaceLoad -> n.name.equals(config.id, ignoreCase = true) }
                .map { n: NetworkInterfaceLoad -> if (n.isUp) UP else DOWN }
                .findFirst()
                .orElse(DOWN)
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value != DOWN
    }
}