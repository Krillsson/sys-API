package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.toFractionalValue
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.docker.ContainerManager

class MonitorInputCreator(
    private val metrics: Metrics,
    private val containerManager: ContainerManager
) {

    private val networkTypes = listOf(
        Monitor.Type.NETWORK_DOWNLOAD_RATE,
        Monitor.Type.NETWORK_UPLOAD_RATE,
        Monitor.Type.NETWORK_UP
    )
    private val diskTypes = listOf(
        Monitor.Type.DISK_READ_RATE,
        Monitor.Type.DISK_WRITE_RATE
    )
    private val fileSystemTypes = listOf(
        Monitor.Type.FILE_SYSTEM_SPACE
    )

    private val processesTypes = listOf(
        Monitor.Type.PROCESS_MEMORY_SPACE,
        Monitor.Type.PROCESS_CPU_LOAD,
        Monitor.Type.PROCESS_EXISTS
    )

    private val containerTypes = listOf<Monitor.Type>(
        Monitor.Type.CONTAINER_RUNNING
    )

    private val containerStatisticsTypes = listOf<Monitor.Type>(
        Monitor.Type.CONTAINER_MEMORY_SPACE, Monitor.Type.CONTAINER_CPU_LOAD
    )

    private fun List<Monitor<*>>.idsSubset(types: List<Monitor.Type>) =
        filter { types.contains(it.type) }
            .mapNotNull { it.config.monitoredItemId }

    fun createInput(
        activeTypes: List<Monitor<*>>,
    ): MonitorInput {
        val containerIds = activeTypes.idsSubset(containerTypes)
        val containerStatisticsIds = activeTypes.idsSubset(containerStatisticsTypes)
        val nicLoads = if (activeTypes.any { networkTypes.contains(it.type) }) metrics.networkMetrics()
            .networkInterfaceLoads() else emptyList()
        val diskLoads =
            if (activeTypes.any { diskTypes.contains(it.type) }) metrics.diskMetrics().diskLoads() else emptyList()
        val fileSystemLoads = if (activeTypes.any { fileSystemTypes.contains(it.type) }) metrics.fileSystemMetrics()
            .fileSystemLoads() else emptyList()
        val processes = activeTypes.filter { processesTypes.contains(it.type) }.mapNotNull {
            it.config.monitoredItemId?.toInt()?.let { pid ->
                metrics.processesMetrics().getProcessByPid(pid).orElse(null)
            }
        }

        val load = SystemLoad(
            uptime = metrics.cpuMetrics().uptime(),
            systemLoadAverage = metrics.cpuMetrics().cpuLoad().systemLoadAverage,
            cpuLoad = metrics.cpuMetrics().cpuLoad(),
            networkInterfaceLoads = nicLoads,
            connectivity = metrics.networkMetrics().connectivity(),
            driveLoads = emptyList(),
            diskLoads = diskLoads,
            fileSystemLoads = fileSystemLoads,
            memory = metrics.memoryMetrics().memoryLoad(),
            processes = processes,
            gpuLoads = emptyList(),
            motherboardHealth = metrics.motherboardMetrics().motherboardHealth()
        )
        val containers =
            if (containerIds.isNotEmpty()) containerManager.containersWithIds(containerIds) else emptyList()
        val containersStats = containerStatisticsIds.mapNotNull { id -> containerManager.statsForContainer(id) }

        return MonitorInput(
            load,
            containers,
            containersStats
        )
    }

    fun getMonitorableItemForType(type: Monitor.Type): List<MonitorableItem> {
        when (type) {
            Monitor.Type.LOAD_AVERAGE_ONE_MINUTE -> {
                val cpuInfo = metrics.cpuMetrics().cpuInfo()
                val cpuMetrics = metrics.cpuMetrics().cpuLoad()
                listOf(
                    MonitorableItem(
                        null,
                        cpuInfo.centralProcessor.name ?: cpuInfo.centralProcessor.model ?: "",
                        null,
                        100f.toFractionalValue(),
                        cpuMetrics.loadAverages.oneMinute.toFractionalValue(),
                        type
                    )
                )
            }
            Monitor.Type.CPU_LOAD -> {
                val cpuInfo = metrics.cpuMetrics().cpuInfo()
                val cpuMetrics = metrics.cpuMetrics().cpuLoad()
                listOf(
                    MonitorableItem(
                        null,
                        cpuInfo.centralProcessor.name ?: cpuInfo.centralProcessor.model ?: "",
                        null,
                        100f.toFractionalValue(),
                        cpuMetrics.usagePercentage.toFractionalValue(),
                        type
                    )
                )
            }
            Monitor.Type.LOAD_AVERAGE_FIVE_MINUTES -> {
                val cpuInfo = metrics.cpuMetrics().cpuInfo()
                val cpuMetrics = metrics.cpuMetrics().cpuLoad()
                listOf(
                    MonitorableItem(
                        null,
                        cpuInfo.centralProcessor.name ?: cpuInfo.centralProcessor.model ?: "",
                        null,
                        100f.toFractionalValue(),
                        cpuMetrics.loadAverages.fiveMinutes.toFractionalValue(),
                        type
                    )
                )
            }
            Monitor.Type.LOAD_AVERAGE_FIFTEEN_MINUTES -> TODO()
            Monitor.Type.CPU_TEMP -> {
                val cpuInfo = metrics.cpuMetrics().cpuInfo()
                val cpuMetrics = metrics.cpuMetrics().cpuLoad()
                listOf(
                    MonitorableItem(
                        null,
                        cpuInfo.centralProcessor.name ?: cpuInfo.centralProcessor.model ?: "",
                        null,
                        100f.toFractionalValue(),
                        cpuMetrics.cpuHealth.temperatures.average().toFractionalValue(),
                        type
                    )
                )
            }
            Monitor.Type.DRIVE_SPACE -> TODO()
            Monitor.Type.FILE_SYSTEM_SPACE -> TODO()
            Monitor.Type.DRIVE_READ_RATE -> TODO()
            Monitor.Type.DISK_READ_RATE -> TODO()
            Monitor.Type.DRIVE_WRITE_RATE -> TODO()
            Monitor.Type.DISK_WRITE_RATE -> TODO()
            Monitor.Type.MEMORY_SPACE -> TODO()
            Monitor.Type.NETWORK_UP -> TODO()
            Monitor.Type.NETWORK_UPLOAD_RATE -> TODO()
            Monitor.Type.NETWORK_DOWNLOAD_RATE -> TODO()
            Monitor.Type.CONTAINER_RUNNING -> TODO()
            Monitor.Type.CONTAINER_MEMORY_SPACE -> TODO()
            Monitor.Type.CONTAINER_CPU_LOAD -> TODO()
            Monitor.Type.PROCESS_MEMORY_SPACE -> TODO()
            Monitor.Type.PROCESS_CPU_LOAD -> TODO()
            Monitor.Type.PROCESS_EXISTS -> TODO()
            Monitor.Type.CONNECTIVITY -> TODO()
            Monitor.Type.EXTERNAL_IP_CHANGED -> TODO()
        }
    }
}