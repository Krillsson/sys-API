package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.history.db.*
import java.time.OffsetDateTime
import java.util.*

fun SystemHistoryEntry.asEntity(): HistorySystemLoadEntity {
    return value.asEntity(UUID.randomUUID(), date)
}

fun HistorySystemLoad.asEntity(id: UUID, dateTime: OffsetDateTime): HistorySystemLoadEntity {
    return HistorySystemLoadEntity(
        id,
        dateTime,
        uptime,
        systemLoadAverage,
        cpuLoad.asCpuLoadEntity(id),
        networkInterfaceLoads.map { it.asNetworkInterfaceLoad(id) },
        connectivity.asConnectivity(id),
        driveLoads.map { it.asDriveLoad(id) },
        memory.asMemoryLoad(id),
        gpuLoads.map { it.asGpuLoad(id) },
        motherboardHealth.map { it.asMotherboardHealthData(id) }
    )
}

private fun com.krillsson.sysapi.core.domain.sensors.HealthData.asMotherboardHealthData(id: UUID): HealthData {
    return HealthData(
        UUID.randomUUID(),
        null,
        id,
        description,
        data,
        dataType.asDataType()
    )
}

private fun com.krillsson.sysapi.core.domain.gpu.GpuLoad.asGpuLoad(id: UUID): GpuLoad {
    return GpuLoad(
        UUID.randomUUID(),
        null,
        id,
        name,
        coreLoad,
        memoryLoad,
        health.asGpuHealth()
    )
}

private fun com.krillsson.sysapi.core.domain.gpu.GpuHealth.asGpuHealth(): GpuHealth {
    return GpuHealth(
        fanRpm, fanPercent, temperature
    )
}

private fun com.krillsson.sysapi.core.domain.memory.MemoryLoad.asMemoryLoad(id: UUID): MemoryLoad {
    return MemoryLoad(
        id,
        numberOfProcesses,
        swapTotalBytes,
        swapUsedBytes,
        totalBytes,
        availableBytes,
        usedPercent
    )
}

private fun com.krillsson.sysapi.core.domain.drives.DriveLoad.asDriveLoad(id: UUID): DriveLoad {
    val uuid = UUID.randomUUID()
    return DriveLoad(
        uuid,
        null,
        id,
        name,
        serial,
        values.asDriveValues(),
        speed.asSpeed(),
        health.temperature,
        health.healthData.map { it.asHealthData(id) }

    )
}

private fun com.krillsson.sysapi.core.domain.sensors.HealthData.asHealthData(id: UUID): DriveHealthData {
    return DriveHealthData(
        UUID.randomUUID(),
        null,
        id,
        description,
        data,
        dataType.asDataType()
    )
}

private fun com.krillsson.sysapi.core.domain.sensors.DataType.asDataType(): DataType {
    return when (this) {
        com.krillsson.sysapi.core.domain.sensors.DataType.CLOCK -> DataType.CLOCK
        com.krillsson.sysapi.core.domain.sensors.DataType.VOLTAGE -> DataType.VOLTAGE
        com.krillsson.sysapi.core.domain.sensors.DataType.PERCENT -> DataType.PERCENT
        com.krillsson.sysapi.core.domain.sensors.DataType.RPM -> DataType.RPM
        com.krillsson.sysapi.core.domain.sensors.DataType.CELCIUS -> DataType.CELCIUS
        com.krillsson.sysapi.core.domain.sensors.DataType.GIGABYTE -> DataType.GIGABYTE
    }
}

private fun com.krillsson.sysapi.core.domain.drives.DriveSpeed.asSpeed(): DriveSpeed {
    return DriveSpeed(
        readBytesPerSecond, writeBytesPerSecond
    )
}

private fun com.krillsson.sysapi.core.domain.drives.DriveValues.asDriveValues(): DriveValues {
    return DriveValues(
        usableSpace, totalSpace, openFileDescriptors, maxFileDescriptors, reads, readBytes, writes, writeBytes
    )
}

private fun com.krillsson.sysapi.core.domain.network.Connectivity.asConnectivity(id: UUID): Connectivity {
    return Connectivity(
        id,
        externalIp,
        previousExternalIp,
        connected
    )
}

private fun com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad.asNetworkInterfaceLoad(id: UUID): NetworkInterfaceLoad {
    return NetworkInterfaceLoad(
        UUID.randomUUID(),
        null,
        id,
        name,
        mac,
        isUp,
        values.asNetworkInterfaceValues(),
        speed.asSpeed(),
    )
}

private fun com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed.asSpeed(): NetworkInterfaceSpeed {
    return NetworkInterfaceSpeed(
        receiveBytesPerSecond, sendBytesPerSecond
    )
}

private fun com.krillsson.sysapi.core.domain.network.NetworkInterfaceValues.asNetworkInterfaceValues(): NetworkInterfaceValues {
    return NetworkInterfaceValues(
        speed, bytesReceived, bytesSent, packetsReceived, packetsSent, inErrors, outErrors
    )
}

private fun com.krillsson.sysapi.core.domain.cpu.CpuLoad.asCpuLoadEntity(id: UUID): CpuLoad {
    return CpuLoad(
        id,
        usagePercentage,
        systemLoadAverage,
        coreLoads.map { it.asCoreLoad(id) },
        cpuHealth.asCpuHealth(id),
        processCount,
        threadCount
    )
}

private fun com.krillsson.sysapi.core.domain.cpu.CpuHealth.asCpuHealth(id: UUID): CpuHealth {
    return CpuHealth(
        id,
        temperatures,
        voltage,
        fanRpm,
        fanPercent
    )
}

private fun com.krillsson.sysapi.core.domain.cpu.CoreLoad.asCoreLoad(id: UUID): CoreLoad {
    return CoreLoad(
        UUID.randomUUID(),
        null,
        id,
        percentage
    )
}