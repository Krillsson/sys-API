package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.MonitorInput
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.Duration
import java.util.*

class CpuMonitor(override val id: UUID, override val inertia: Duration, override val threshold: Double) : MonitorInput {

    override val type: MonitorType = MonitorType.CPU_LOAD

    override fun value(systemLoad: SystemLoad): Double {
        return systemLoad.cpuLoad.cpuLoadOsMxBean
    }

    override fun isPastThreshold(value: Double): Boolean {
        return value > threshold
    }
}