package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.cpu.CoreLoad
import com.krillsson.sysapi.core.domain.cpu.CpuHealth
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.drives.DriveHealth
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.sensors.HealthData
import com.krillsson.sysapi.core.history.db.*

fun HistorySystemLoadEntity.asSystemHistoryEntry(): SystemHistoryEntry {
    return SystemHistoryEntry(
        date,
        HistorySystemLoad(
            uptime,
            systemLoadAverage,
            cpuLoad.asCpuLoad(),
            networkInterfaceLoads.map { it.asNetworkInterfaceLoad() },
            connectivity.asConnectivity(),
            driveLoads.map { it.asDriveLoad() },
            memory.asMemoryLoad(),
            gpuLoads.map { it.asGpuLoad() },
            motherboardHealth.map { it.asHealthData() }
        )
    )
}

private fun com.krillsson.sysapi.core.history.db.HealthData.asHealthData(): HealthData {
    return HealthData(
        description, data, dataType.asDataType()
    )
}

private fun GpuLoad.asGpuLoad(): com.krillsson.sysapi.core.domain.gpu.GpuLoad {
    return com.krillsson.sysapi.core.domain.gpu.GpuLoad(
        name, coreLoad, memoryLoad, health.asHealth()
    )
}

private fun GpuHealth.asHealth(): com.krillsson.sysapi.core.domain.gpu.GpuHealth {
    return com.krillsson.sysapi.core.domain.gpu.GpuHealth(
        fanRpm, fanPercent, temperature
    )
}

private fun MemoryLoad.asMemoryLoad(): com.krillsson.sysapi.core.domain.memory.MemoryLoad {
    return com.krillsson.sysapi.core.domain.memory.MemoryLoad(
        numberOfProcesses,
        swapTotalBytes,
        swapUsedBytes,
        totalBytes,
        availableBytes,
        usedPercent
    )
}

private fun DriveLoad.asDriveLoad(): com.krillsson.sysapi.core.domain.drives.DriveLoad {
    return com.krillsson.sysapi.core.domain.drives.DriveLoad(
        name,
        serial,
        values.asValues(),
        speed.asSpeed(),
        asHealthData()
    )
}

private fun DriveLoad.asHealthData(): DriveHealth {
    return DriveHealth(
        temperature, healthData.map { it.asHealthData() }
    )
}

private fun DriveHealthData.asHealthData(): HealthData {
    return HealthData(description, data, dataType.asDataType())
}

private fun DataType.asDataType(): com.krillsson.sysapi.core.domain.sensors.DataType {
    return when (this) {
        DataType.CLOCK -> com.krillsson.sysapi.core.domain.sensors.DataType.CLOCK
        DataType.VOLTAGE -> com.krillsson.sysapi.core.domain.sensors.DataType.VOLTAGE
        DataType.PERCENT -> com.krillsson.sysapi.core.domain.sensors.DataType.PERCENT
        DataType.RPM -> com.krillsson.sysapi.core.domain.sensors.DataType.RPM
        DataType.CELCIUS -> com.krillsson.sysapi.core.domain.sensors.DataType.CELCIUS
        DataType.GIGABYTE -> com.krillsson.sysapi.core.domain.sensors.DataType.GIGABYTE
    }
}

private fun DriveSpeed.asSpeed(): com.krillsson.sysapi.core.domain.drives.DriveSpeed {
    return com.krillsson.sysapi.core.domain.drives.DriveSpeed(readBytesPerSecond, writeBytesPerSecond)
}

private fun DriveValues.asValues(): com.krillsson.sysapi.core.domain.drives.DriveValues {
    return com.krillsson.sysapi.core.domain.drives.DriveValues(
        usableSpace, totalSpace, openFileDescriptors, maxFileDescriptors, reads, readBytes, writes, writeBytes
    )
}

private fun Connectivity.asConnectivity(): com.krillsson.sysapi.core.domain.network.Connectivity {
    return com.krillsson.sysapi.core.domain.network.Connectivity(
        externalIp, previousExternalIp, connected
    )
}

private fun NetworkInterfaceLoad.asNetworkInterfaceLoad(): com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad {
    return com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad(
        name,
        mac,
        isUp,
        values.asNetworkInterfaceValues(),
        speed.asNetworkInterfaceSpeed()
    )
}

private fun NetworkInterfaceSpeed.asNetworkInterfaceSpeed(): com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed {
    return com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed(receiveBytesPerSecond, sendBytesPerSecond)
}

private fun NetworkInterfaceValues.asNetworkInterfaceValues(): com.krillsson.sysapi.core.domain.network.NetworkInterfaceValues {
    return com.krillsson.sysapi.core.domain.network.NetworkInterfaceValues(
        speed, bytesReceived, bytesSent, packetsReceived, packetsSent, inErrors, outErrors
    )
}

private fun com.krillsson.sysapi.core.history.db.CpuLoad.asCpuLoad(): CpuLoad {
    return CpuLoad(
        usagePercentage,
        systemLoadAverage,
        coreLoads.map { it.asCoreLoad() },
        cpuHealth.asCpuHealth(),
        processCount,
        threadCount
    )
}

private fun com.krillsson.sysapi.core.history.db.CpuHealth.asCpuHealth(): CpuHealth {
    return CpuHealth(
        temperatures, voltage, fanRpm, fanPercent
    )
}

private fun com.krillsson.sysapi.core.history.db.CoreLoad.asCoreLoad(): CoreLoad {
    return CoreLoad(
        percentage
    )
}