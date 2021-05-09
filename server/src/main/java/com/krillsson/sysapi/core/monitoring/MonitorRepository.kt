package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.monitoring.monitors.*
import com.krillsson.sysapi.persistence.Store

class MonitorRepository(private val store: Store<List<MonitorStore.StoredMonitor>>) {

    fun read(): List<Monitor> {
        return store.read()?.map { it.asMonitor() }.orEmpty()
    }

    fun write(content: List<Monitor>) {
        store.write(content.map { it.asStoredMonitor() })
    }

    fun update(action: (List<Monitor>?) -> List<Monitor>) {
        val previousValue = read()
        val newValue = action(previousValue)
        write(newValue)
    }

    private fun Monitor.asStoredMonitor(): MonitorStore.StoredMonitor {
        return when (this) {
            is CpuMonitor -> MonitorStore.StoredMonitor(id, type.asStoredType(), config.asStoredMonitorConfig())
            is CpuTemperatureMonitor -> MonitorStore.StoredMonitor(
                id,
                type.asStoredType(),
                config.asStoredMonitorConfig()
            )
            is DriveMonitor -> MonitorStore.StoredMonitor(id, type.asStoredType(), config.asStoredMonitorConfig())
            is MemoryMonitor -> MonitorStore.StoredMonitor(id, type.asStoredType(), config.asStoredMonitorConfig())
            is NetworkUpMonitor -> MonitorStore.StoredMonitor(id, type.asStoredType(), config.asStoredMonitorConfig())
            else -> throw IllegalArgumentException("Unknown monitor type encountered $this")
        }
    }

    private fun MonitorConfig.asStoredMonitorConfig(): MonitorStore.StoredMonitor.Config {
        return MonitorStore.StoredMonitor.Config(
            monitoredItemId, threshold, inertia
        )
    }

    private fun MonitorStore.StoredMonitor.asMonitor(): Monitor {
        return when (type) {
            MonitorStore.StoredMonitor.Type.CPU_LOAD -> CpuMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.CPU_TEMP -> CpuTemperatureMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.DRIVE_SPACE -> DriveMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.MEMORY_SPACE -> MemoryMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.NETWORK_UP -> NetworkUpMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.CONTAINER_RUNNING -> DockerContainerRunningMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.PROCESS_CPU_LOAD -> ProcessCpuMonitor(id, config.asConfig())
            MonitorStore.StoredMonitor.Type.PROCESS_MEMORY_SPACE -> ProcessMemoryMonitor(id, config.asConfig())

        }
    }

    private fun MonitorStore.StoredMonitor.Config.asConfig(): MonitorConfig {
        return MonitorConfig(
            monitoredItemId, threshold, inertia
        )
    }

    private fun MonitorType.asStoredType(): MonitorStore.StoredMonitor.Type {
        return when (this) {
            MonitorType.CPU_LOAD -> MonitorStore.StoredMonitor.Type.CPU_LOAD
            MonitorType.CPU_TEMP -> MonitorStore.StoredMonitor.Type.CPU_TEMP
            MonitorType.DRIVE_SPACE -> MonitorStore.StoredMonitor.Type.DRIVE_SPACE
            MonitorType.MEMORY_SPACE -> MonitorStore.StoredMonitor.Type.MEMORY_SPACE
            MonitorType.NETWORK_UP -> MonitorStore.StoredMonitor.Type.NETWORK_UP
            MonitorType.CONTAINER_RUNNING -> MonitorStore.StoredMonitor.Type.CONTAINER_RUNNING
            MonitorType.PROCESS_MEMORY_SPACE -> MonitorStore.StoredMonitor.Type.PROCESS_MEMORY_SPACE
            MonitorType.PROCESS_CPU_LOAD -> MonitorStore.StoredMonitor.Type.PROCESS_CPU_LOAD
        }
    }

}