package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

class CpuMonitor(override val id: UUID, override val config: Config) : Monitor() {

    override val type: MonitorType = MonitorType.CPU_LOAD

    override fun selectValue(load: SystemLoad): Double = load.cpuLoad.cpuLoadOsMxBean

    override fun isPastThreshold(value: Double): Boolean {
        return value > config.threshold
    }
}