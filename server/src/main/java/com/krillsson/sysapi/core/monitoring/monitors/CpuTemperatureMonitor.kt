package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class CpuTemperatureMonitor(override val id: UUID, override val config: Monitor.Config) : Monitor {

    override val type: MonitorType = MonitorType.CPU_TEMP

    override fun selectValue(load: SystemLoad): Double =
            load.cpuLoad.cpuHealth.temperatures.stream().findFirst().orElse(-1.0)

    override fun isPastThreshold(value: Double): Boolean {
        return value > config.threshold
    }
}