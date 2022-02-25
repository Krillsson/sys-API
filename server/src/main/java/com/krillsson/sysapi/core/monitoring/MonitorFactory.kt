package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.monitors.*
import java.util.*

object MonitorFactory {
    fun createMonitor(type: MonitorType, id: UUID, config: MonitorConfig): Monitor {
        return when (type) {
            MonitorType.CPU_LOAD -> CpuMonitor(id, config)
            MonitorType.CPU_TEMP -> CpuTemperatureMonitor(id, config)
            MonitorType.DRIVE_SPACE -> DriveSpaceMonitor(id, config)
            MonitorType.MEMORY_SPACE -> MemoryMonitor(id, config)
            MonitorType.NETWORK_UP -> NetworkUpMonitor(id, config)
            MonitorType.CONTAINER_RUNNING -> DockerContainerRunningMonitor(id, config)
            MonitorType.PROCESS_MEMORY_SPACE -> ProcessMemoryMonitor(id, config)
            MonitorType.PROCESS_CPU_LOAD -> ProcessCpuMonitor(id, config)
            MonitorType.PROCESS_EXISTS -> ProcessExistsMonitor(id, config)
            MonitorType.CONNECTIVITY -> ConnectivityMonitor(id, config)
            MonitorType.EXTERNAL_IP_CHANGED -> ExternalIpChangedMonitor(id, config)
            MonitorType.DRIVE_READ_RATE -> DriveReadRateMonitor(id, config)
            MonitorType.DRIVE_WRITE_RATE -> DriveWriteRateMonitor(id, config)
            MonitorType.NETWORK_UPLOAD_RATE -> NetworkUploadRateMonitor(id, config)
            MonitorType.NETWORK_DOWNLOAD_RATE -> NetworkDownloadRateMonitor(id, config)
        }
    }
}