package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.monitors.CpuMonitor
import com.krillsson.sysapi.core.monitoring.monitors.CpuTemperatureMonitor
import com.krillsson.sysapi.core.monitoring.monitors.DriveMonitor
import com.krillsson.sysapi.core.monitoring.monitors.MemoryMonitor
import com.krillsson.sysapi.core.monitoring.monitors.NetworkUpMonitor
import java.util.*

object MonitorFactory {
    fun createMonitor(type: MonitorType, id: UUID, config: MonitorConfig): Monitor {
        return when (type) {
            MonitorType.CPU_LOAD -> CpuMonitor(
                id,
                config
            )
            MonitorType.CPU_TEMP -> CpuTemperatureMonitor(
                id,
                config
            )
            MonitorType.DRIVE_SPACE -> DriveMonitor(
                id,
                config
            )
            MonitorType.DRIVE_TEMP -> DriveMonitor(
                id,
                config
            )
            MonitorType.GPU_LOAD -> TODO("Not implemented")
            MonitorType.GPU_TEMP -> TODO("Not implemented")
            MonitorType.MEMORY_SPACE -> MemoryMonitor(
                id,
                config
            )
            MonitorType.NETWORK_UP -> NetworkUpMonitor(
                id,
                config
            )
        }
    }
}