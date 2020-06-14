package com.krillsson.sysapi.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.sensors.DataType
import com.krillsson.sysapi.core.domain.system.SystemInfo
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.history.SystemHistoryEntry
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.domain.event.MonitorEvent
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.domain.ProcessSortMethod
import com.krillsson.sysapi.util.EnvironmentUtils
import oshi.hardware.UsbDevice
import oshi.software.os.OperatingSystem

class QueryResolver : GraphQLQueryResolver {

    var metrics: Metrics? = null
    var monitorManager: MonitorManager? = null
    var eventManager: EventManager? = null
    var historyManager: HistoryManager? = null
    var os: OperatingSystem? = null
    val systemInfoResolver = SystemInfoResolver()
    val historyResolver = HistoryResolver()
    val monitorResolver = MonitorResolver()
    val monitorEventResolver = MonitorEventResolver()
    val motherboardResolver = MotherboardResolver()
    val processorResolver = ProcessorResolver()
    val processorMetricsResolver = ProcessorMetricsResolver()
    val driveResolver = DriveResolver()
    val driveMetricResolver = DriveMetricResolver()
    val networkInterfaceResolver = NetworkInterfaceResolver()
    val memoryLoadResolver = MemoryLoadResolver()

    val networkInterfaceMetricResolver = NetworkInterfaceMetricResolver()

    fun system(): SystemInfo {
        val metric =
            checkNotNull(metrics) { "System API did not initialize properly. Metrics is null in QueryResolver" }
        val operatingSystem =
            checkNotNull(os) { "System API did not initialize properly. OperatingSystem is null in QueryResolver" }
        return SystemInfo(
            EnvironmentUtils.getHostName(),
            operatingSystem,
            oshi.SystemInfo.getCurrentPlatformEnum(),
            metric.cpuMetrics().cpuInfo(),
            metric.motherboardMetrics().motherboard(),
            metric.memoryMetrics().memoryLoad(),
            metric.driveMetrics().drives(),
            metric.networkMetrics().networkInterfaces(),
            metric.gpuMetrics().gpus()
        )
    }

    fun history(): List<SystemHistoryEntry> {
        return historyManager?.history?.map { SystemHistoryEntry(it.date, it.value) }?.toList().orEmpty()
    }

    fun monitors(): List<Monitor> {
        return monitorManager?.getAll()?.toList().orEmpty()
    }

    fun events() = eventManager?.getAll()?.toList().orEmpty()

    inner class SystemInfoResolver : GraphQLResolver<SystemInfo> {
        fun processorMetrics(system: SystemInfo) = metrics?.cpuMetrics()?.cpuLoad()

        fun getBaseboard(system: SystemInfo): Motherboard? {
            return metrics?.motherboardMetrics()?.motherboard()
        }

        fun getUsbDevices(system: SystemInfo): List<UsbDevice> {
            return getBaseboard(system)?.usbDevices?.toList().orEmpty()
        }

        fun getUptime(system: SystemInfo): Long? {
            return metrics?.cpuMetrics()?.uptime()
        }

        fun getProcessor(system: SystemInfo): CentralProcessor? {
            return metrics?.cpuMetrics()?.cpuInfo()?.centralProcessor
        }

        fun getGraphics(system: SystemInfo): List<Gpu?>? {
            return metrics?.gpuMetrics()?.gpus()
        }

        fun getProcesses(
            system: SystemInfo,
            limit: Int = 0,
            processSortMethod: ProcessSortMethod = ProcessSortMethod.MEMORY
        ): List<Process?>? {
            return metrics?.processesMetrics()
                ?.processesInfo(processSortMethod.toOperatingSystemProcessSort(), limit)?.processes
        }

        private fun ProcessSortMethod.toOperatingSystemProcessSort(): OperatingSystem.ProcessSort = when (this) {
            ProcessSortMethod.CPU -> OperatingSystem.ProcessSort.CPU
            ProcessSortMethod.MEMORY -> OperatingSystem.ProcessSort.MEMORY
            ProcessSortMethod.OLDEST -> OperatingSystem.ProcessSort.OLDEST
            ProcessSortMethod.NEWEST -> OperatingSystem.ProcessSort.NEWEST
            ProcessSortMethod.PID -> OperatingSystem.ProcessSort.PID
            ProcessSortMethod.PARENTPID -> OperatingSystem.ProcessSort.PARENTPID
            ProcessSortMethod.NAME -> OperatingSystem.ProcessSort.NAME
        }
    }

    inner class HistoryResolver : GraphQLResolver<SystemHistoryEntry> {
        fun getProcessorMetrics(historyEntry: SystemHistoryEntry): CpuLoad? {
            return historyEntry.value.cpuLoad
        }

        fun getDriveMetrics(historyEntry: SystemHistoryEntry): List<DriveLoad> {
            return historyEntry.value.driveLoads
        }

        fun getNetworkInterfaceMetrics(historyEntry: SystemHistoryEntry): List<NetworkInterfaceLoad> {
            return historyEntry.value.networkInterfaceLoads
        }

        fun getGraphicsMetrics(historyEntry: SystemHistoryEntry): List<GpuLoad> {
            return historyEntry.value.gpuLoads
        }
    }

    inner class ProcessorResolver : GraphQLResolver<CentralProcessor> {
        fun getMetrics(processor: CentralProcessor) = metrics?.cpuMetrics()?.cpuLoad()
    }

    inner class ProcessorMetricsResolver : GraphQLResolver<CpuLoad> {
        fun getVoltage(cpuLoad: CpuLoad) = metrics?.cpuMetrics()?.cpuLoad()?.cpuHealth?.voltage
        fun getFanRpm(cpuLoad: CpuLoad) = metrics?.cpuMetrics()?.cpuLoad()?.cpuHealth?.fanRpm
        fun getFanPercent(cpuLoad: CpuLoad) = metrics?.cpuMetrics()?.cpuLoad()?.cpuHealth?.fanPercent
        fun getTemperatures(cpuLoad: CpuLoad) = metrics?.cpuMetrics()?.cpuLoad()?.cpuHealth?.temperatures
    }

    inner class MonitorResolver : GraphQLResolver<Monitor> {
        fun getInertiaInSeconds(monitor: Monitor) = monitor.config.inertia.seconds
        fun getType(monitor: Monitor) = monitor.type
        fun getThreshold(monitor: Monitor) = monitor.config.threshold
    }

    inner class MonitorEventResolver : GraphQLResolver<MonitorEvent> {
        fun getType(monitorEvent: MonitorEvent) = monitorEvent.monitorType
    }

    inner class MotherboardResolver : GraphQLResolver<Motherboard> {
        fun getManufacturer(motherboard: Motherboard) = motherboard.computerSystem.manufacturer
        fun getModel(motherboard: Motherboard) = motherboard.computerSystem.model
        fun getSerialNumber(motherboard: Motherboard) = motherboard.computerSystem.serialNumber
        fun getFirmware(motherboard: Motherboard) = motherboard.computerSystem.firmware
    }

    inner class DriveResolver : GraphQLResolver<Drive> {
        fun getId(drive: Drive) = drive.serial
        fun getMetrics(drive: Drive) = metrics?.driveMetrics()?.driveLoadByName(drive.serial)
    }

    inner class DriveMetricResolver : GraphQLResolver<DriveLoad> {
        fun getDriveId(driveLoad: DriveLoad) = driveLoad.serial
        fun getTemperature(driveLoad: DriveLoad) = driveLoad.health.temperature
        fun getHealthData(driveLoad: DriveLoad) = driveLoad.health.healthData.map { DriveHealth(it.data, it.dataType) }
        fun getUsableSpace(driveLoad: DriveLoad) = driveLoad.values.usableSpace
        fun getTotalSpace(driveLoad: DriveLoad) = driveLoad.values.totalSpace
        fun getOpenFileDescriptors(driveLoad: DriveLoad) = driveLoad.values.openFileDescriptors
        fun getMaxFileDescriptors(driveLoad: DriveLoad) = driveLoad.values.maxFileDescriptors
        fun getReads(driveLoad: DriveLoad) = driveLoad.values.reads
        fun getWrites(driveLoad: DriveLoad) = driveLoad.values.writes
        fun getReadBytes(driveLoad: DriveLoad) = driveLoad.values.readBytes
        fun getWriteBytes(driveLoad: DriveLoad) = driveLoad.values.writeBytes
        fun getCurrentReadWriteRate(driveLoad: DriveLoad) =
            driveLoad.speed.let { DriveReadWriteRate(it.readBytesPerSecond, it.writeBytesPerSecond) }
    }

    data class DriveHealth(
        val value: Double,
        val type: DataType
    )

    data class DriveReadWriteRate(val readBytesPerSecond: Long, val writeBytesPerSecond: Long)

    inner class NetworkInterfaceResolver : GraphQLResolver<NetworkInterface> {
        fun getId(networkInterface: NetworkInterface) = networkInterface.name
        fun getMetrics(networkInterface: NetworkInterface) =
            metrics?.networkMetrics()?.networkInterfaceLoadById(networkInterface.name)
    }

    inner class NetworkInterfaceMetricResolver : GraphQLResolver<NetworkInterfaceLoad> {
        fun getNetworkInterfaceid(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.name
        fun getBytesReceived(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.values.bytesReceived
        fun getBytesSent(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.values.bytesSent
        fun getPacketsReceived(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.values.packetsReceived
        fun getPacketsSent(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.values.packetsSent
        fun getInErrors(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.values.inErrors
        fun getOutErrors(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.values.outErrors
        fun getReadWriteRate(networkInterfaceLoad: NetworkInterfaceLoad) = networkInterfaceLoad.speed.let {
            NetworkInterfaceReadWriteRate(
                it.receiveBytesPerSecond,
                it.sendBytesPerSecond
            )
        }
    }

    data class NetworkInterfaceReadWriteRate(val receiveBytesPerSecond: Long, val sendBytesPerSecond: Long)

    inner class MemoryLoadResolver : GraphQLResolver<MemoryLoad> {
        fun getSwapTotalBytes(memoryLoad: MemoryLoad) = memoryLoad.swapTotal
        fun getTotalBytes(memoryLoad: MemoryLoad) = memoryLoad.total
        fun getMetrics(memoryLoad: MemoryLoad) =
            MemoryMetrics(memoryLoad.numberOfProcesses, memoryLoad.swapUsed, memoryLoad.available)
    }

    data class MemoryMetrics(
        val numberOfProcesses: Int,
        val swapUsedBytes: Long,
        val availableBytes: Long
    )
}