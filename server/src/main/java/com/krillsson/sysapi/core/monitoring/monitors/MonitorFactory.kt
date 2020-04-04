package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.monitoring.MonitorType
import java.util.*

object MonitorFactory {
    fun createMonitor(type: MonitorType, id: UUID, config: com.krillsson.sysapi.core.monitoring.Monitor.Config): com.krillsson.sysapi.core.monitoring.Monitor {
        return when (type) {
            MonitorType.CPU_LOAD -> CpuMonitor(id, config)
            MonitorType.CPU_TEMP -> CpuTemperatureMonitor(id, config)
            MonitorType.DRIVE_SPACE -> DriveMonitor(id, config)
            MonitorType.DRIVE_TEMP -> DriveMonitor(id, config)
            MonitorType.GPU_LOAD -> TODO("Not implemented")
            MonitorType.GPU_TEMP -> TODO("Not implemented")
            MonitorType.MEMORY_SPACE -> MemoryMonitor(id, config)
            MonitorType.NETWORK_UP -> NetworkUpMonitor(id, config)
        }
    }
}