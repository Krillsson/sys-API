package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.monitors.*
import java.util.*

object MonitorFactory {
    fun createMonitor(type: Monitor.Type, id: UUID, config: MonitorConfig): Monitor {
        return when (type) {
            Monitor.Type.CPU_LOAD -> CpuMonitor(id, config)
            Monitor.Type.CPU_TEMP -> CpuTemperatureMonitor(id, config)
            Monitor.Type.DRIVE_SPACE -> DriveSpaceMonitor(id, config)
            Monitor.Type.MEMORY_SPACE -> MemoryMonitor(id, config)
            Monitor.Type.NETWORK_UP -> NetworkUpMonitor(id, config)
            Monitor.Type.CONTAINER_RUNNING -> DockerContainerRunningMonitor(id, config)
            Monitor.Type.PROCESS_MEMORY_SPACE -> ProcessMemoryMonitor(id, config)
            Monitor.Type.PROCESS_CPU_LOAD -> ProcessCpuMonitor(id, config)
            Monitor.Type.PROCESS_EXISTS -> ProcessExistsMonitor(id, config)
            Monitor.Type.CONNECTIVITY -> ConnectivityMonitor(id, config)
            Monitor.Type.EXTERNAL_IP_CHANGED -> ExternalIpChangedMonitor(id, config)
            Monitor.Type.DRIVE_READ_RATE -> DriveReadRateMonitor(id, config)
            Monitor.Type.DRIVE_WRITE_RATE -> DriveWriteRateMonitor(id, config)
            Monitor.Type.NETWORK_UPLOAD_RATE -> NetworkUploadRateMonitor(id, config)
            Monitor.Type.NETWORK_DOWNLOAD_RATE -> NetworkDownloadRateMonitor(id, config)
        }
    }
}