package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.MonitorInput
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.Duration
import java.util.*

class CpuTemperatureMonitor(override val id: UUID, override val inertia: Duration, override val threshold: Double) : MonitorInput {
    override val type: MonitorType = MonitorType.CPU_TEMP

    override fun value(systemLoad: SystemLoad): Double {
        return systemLoad.cpuLoad.cpuHealth.temperatures.stream().findFirst().orElse(-1.0)
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value > threshold
    }
}