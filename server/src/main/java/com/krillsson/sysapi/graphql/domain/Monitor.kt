package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.monitoring.monitors.*
import java.time.OffsetDateTime
import java.util.*

interface MonitoredValue

data class NumericalValue(val number: Long) : com.krillsson.sysapi.graphql.domain.MonitoredValue
data class FractionalValue(val fraction: Float) : com.krillsson.sysapi.graphql.domain.MonitoredValue
data class ConditionalValue(val condition: Boolean) : com.krillsson.sysapi.graphql.domain.MonitoredValue

data class MonitoredValueHistoryEntry(
    val date: OffsetDateTime,
    val value: com.krillsson.sysapi.graphql.domain.MonitoredValue
)

data class Monitor(
    val id: UUID,
    val inertiaInSeconds: Int,
    val monitoredItemId: String?,
    val threshold: com.krillsson.sysapi.graphql.domain.MonitoredValue,
    val type: com.krillsson.sysapi.core.monitoring.Monitor.Type
)

fun com.krillsson.sysapi.core.monitoring.Monitor<MonitoredValue>.asMonitor(): Monitor {
    return Monitor(
        id,
        config.inertia.seconds.toInt(),
        config.monitoredItemId,
        config.threshold.asMonitoredValue(),
        type
    )
}

fun MonitoredValue.asMonitoredValue(): com.krillsson.sysapi.graphql.domain.MonitoredValue {
    return when(this) {
        is MonitoredValue.ConditionalValue -> ConditionalValue(value)
        is MonitoredValue.FractionalValue -> FractionalValue(value)
        is MonitoredValue.NumericalValue -> NumericalValue(value)
    }
}

object Selectors {


    fun forConditionalMonitorType(type: com.krillsson.sysapi.core.monitoring.Monitor.Type): ConditionalValueSelector = when (type) {
        com.krillsson.sysapi.core.monitoring.Monitor.Type.NETWORK_UP -> NetworkUpMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.PROCESS_EXISTS -> ProcessExistsMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.CONNECTIVITY -> ConnectivityMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.EXTERNAL_IP_CHANGED -> ExternalIpChangedMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.CONTAINER_RUNNING -> throw IllegalArgumentException("Use forContainerConditionalMonitor() method")
        else -> throw IllegalArgumentException("$type is a ${type.valueType} type")
    }

    fun forNumericalMonitorType(type: com.krillsson.sysapi.core.monitoring.Monitor.Type): NumericalValueSelector = when (type) {
        com.krillsson.sysapi.core.monitoring.Monitor.Type.CPU_TEMP -> CpuTemperatureMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.DRIVE_SPACE -> DriveSpaceMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.DRIVE_READ_RATE -> DriveReadRateMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.DRIVE_WRITE_RATE -> DriveWriteRateMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.MEMORY_SPACE -> MemorySpaceMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.NETWORK_UPLOAD_RATE -> NetworkUploadRateMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.NETWORK_DOWNLOAD_RATE -> NetworkDownloadRateMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.PROCESS_MEMORY_SPACE -> ProcessMemoryMonitor.selector
        else -> throw IllegalArgumentException("$type is a ${type.valueType} type")
    }

    fun forFractionalMonitorType(type: com.krillsson.sysapi.core.monitoring.Monitor.Type): FractionalValueSelector = when (type) {
        com.krillsson.sysapi.core.monitoring.Monitor.Type.CPU_LOAD -> CpuMonitor.selector
        com.krillsson.sysapi.core.monitoring.Monitor.Type.PROCESS_CPU_LOAD -> ProcessCpuMonitor.selector
        else -> throw IllegalArgumentException("$type is a ${type.valueType} type")
    }

    fun forContainerConditionalMonitor(type: com.krillsson.sysapi.core.monitoring.Monitor.Type): ContainerConditionalValueSelector =
        when (type) {
            com.krillsson.sysapi.core.monitoring.Monitor.Type.CONTAINER_RUNNING -> DockerContainerRunningMonitor.selector
            else -> throw IllegalArgumentException("Use forConditionalMonitorType() method")
        }
}

