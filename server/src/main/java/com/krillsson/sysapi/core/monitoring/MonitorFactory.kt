package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.monitoring.monitors.*
import java.util.*

object MonitorFactory {
    @Suppress("UNCHECKED_CAST")
    // TODO: create three separate methods(?)
    fun createMonitor(type: Monitor.Type, id: UUID, config: MonitorConfig<MonitoredValue>): Monitor<MonitoredValue> {
        return when (type) {
            Monitor.Type.CPU_LOAD -> CpuMonitor(id, config as MonitorConfig<MonitoredValue.FractionalValue>)
            Monitor.Type.CPU_TEMP -> CpuTemperatureMonitor(id, config as MonitorConfig<MonitoredValue.NumericalValue>)
            Monitor.Type.DRIVE_SPACE -> DriveSpaceMonitor(id, config as MonitorConfig<MonitoredValue.NumericalValue>)
            Monitor.Type.MEMORY_SPACE -> MemorySpaceMonitor(id, config as MonitorConfig<MonitoredValue.NumericalValue>)
            Monitor.Type.NETWORK_UP -> NetworkUpMonitor(id, config as MonitorConfig<MonitoredValue.ConditionalValue>)
            Monitor.Type.CONTAINER_RUNNING -> DockerContainerRunningMonitor(
                id,
                config as MonitorConfig<MonitoredValue.ConditionalValue>
            )

            Monitor.Type.PROCESS_MEMORY_SPACE -> ProcessMemoryMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.PROCESS_CPU_LOAD -> ProcessCpuMonitor(
                id,
                config as MonitorConfig<MonitoredValue.FractionalValue>
            )

            Monitor.Type.PROCESS_EXISTS -> ProcessExistsMonitor(
                id,
                config as MonitorConfig<MonitoredValue.ConditionalValue>
            )

            Monitor.Type.CONNECTIVITY -> ConnectivityMonitor(
                id,
                config as MonitorConfig<MonitoredValue.ConditionalValue>
            )

            Monitor.Type.EXTERNAL_IP_CHANGED -> ExternalIpChangedMonitor(
                id,
                config as MonitorConfig<MonitoredValue.ConditionalValue>
            )

            Monitor.Type.DRIVE_READ_RATE -> DriveReadRateMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.DRIVE_WRITE_RATE -> DriveWriteRateMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.NETWORK_UPLOAD_RATE -> NetworkUploadRateMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.NETWORK_DOWNLOAD_RATE -> NetworkDownloadRateMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.FILE_SYSTEM_SPACE -> FileSystemSpaceMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.DISK_READ_RATE -> DiskReadRateMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.DISK_WRITE_RATE -> DiskWriteRateMonitor(
                id,
                config as MonitorConfig<MonitoredValue.NumericalValue>
            )

            Monitor.Type.LOAD_AVERAGE_ONE_MINUTE -> LoadAverageMonitorOneMinute(
                id,
                config as MonitorConfig<MonitoredValue.FractionalValue>
            )

            Monitor.Type.LOAD_AVERAGE_FIVE_MINUTES -> LoadAverageMonitorFiveMinutes(
                id,
                config as MonitorConfig<MonitoredValue.FractionalValue>
            )

            Monitor.Type.LOAD_AVERAGE_FIFTEEN_MINUTES -> LoadAverageMonitorFifteenMinutes(
                id,
                config as MonitorConfig<MonitoredValue.FractionalValue>
            )
        }
    }
}