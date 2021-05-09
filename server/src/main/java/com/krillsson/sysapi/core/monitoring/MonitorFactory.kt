package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.monitors.*
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
            MonitorType.MEMORY_SPACE -> MemoryMonitor(
                id,
                config
            )
            MonitorType.NETWORK_UP -> NetworkUpMonitor(
                id,
                config
            )
            MonitorType.CONTAINER_RUNNING -> DockerContainerRunningMonitor(id, config)
        }
    }
}