package com.krillsson.sysapi.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.event.MonitorEvent
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.memory.MemoryInfo
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.sensors.DataType
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.util.EnvironmentUtils
import oshi.hardware.UsbDevice

class QueryResolver : GraphQLQueryResolver {

    lateinit var metrics: Metrics
    lateinit var monitorManager: MonitorManager
    lateinit var eventManager: EventManager
    lateinit var historyManager: HistoryManager
    lateinit var operatingSystem: OperatingSystem
    lateinit var platform: Platform

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        historyManager: HistoryManager,
        operatingSystem: OperatingSystem,
        platform: Platform
    ) {
        this.metrics = metrics
        this.monitorManager = monitorManager
        this.eventManager = eventManager
        this.historyManager = historyManager
        this.operatingSystem = operatingSystem
        this.platform = platform
    }

    val systemInfoResolver = SystemResolver()
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
    val memoryInfoResolver = MemoryInfoResolver()
    val networkInterfaceMetricResolver = NetworkInterfaceMetricResolver()

    data class System(
        val hostName: String,
        val operatingSystem: OperatingSystem,
        val platform: Platform
    )

    fun system(): System = System(EnvironmentUtils.getHostName(), operatingSystem, platform)

    fun history(): List<SystemHistoryEntry> {
        return historyManager.getHistory().map {
            SystemHistoryEntry(
                it.date,
                it.value
            )
        }.toList()
    }

    fun monitors(): List<Monitor> {
        return monitorManager.getAll().toList()
    }

    fun events() = eventManager.getAll().toList()

    inner class SystemResolver : GraphQLResolver<System> {
        fun processorMetrics(system: System) = metrics.cpuMetrics().cpuLoad()

        fun getBaseboard(system: System): Motherboard {
            return metrics.motherboardMetrics().motherboard()
        }

        fun getHostname(system: System): String {
            return system.hostName
        }

        fun getUsbDevices(system: System): List<UsbDevice> {
            return getBaseboard(system)?.usbDevices?.toList().orEmpty()
        }

        fun getUptime(system: System): Long {
            return metrics.cpuMetrics().uptime()
        }

        fun getProcessor(system: System): CentralProcessor {
            return metrics.cpuMetrics().cpuInfo().centralProcessor
        }

        fun getGraphics(system: System): List<Gpu> {
            return metrics.gpuMetrics().gpus()
        }

        fun getDrives(system: System) = metrics.driveMetrics().drives()

        fun getProcesses(
            system: System,
            limit: Int = 0,
            processSortMethod: ProcessSort = ProcessSort.MEMORY
        ): List<Process?>? {
            return metrics.processesMetrics()
                .processesInfo(processSortMethod, limit).processes
        }

        fun networkInterfaces(system: System) = metrics.networkMetrics().networkInterfaces()
        fun getMemory(system: System) = metrics.memoryMetrics().memoryInfo()
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
        fun getMetrics(processor: CentralProcessor) = metrics.cpuMetrics().cpuLoad()
    }

    inner class ProcessorMetricsResolver : GraphQLResolver<CpuLoad> {
        fun getVoltage(cpuLoad: CpuLoad) = metrics.cpuMetrics().cpuLoad().cpuHealth.voltage
        fun getFanRpm(cpuLoad: CpuLoad) = metrics.cpuMetrics().cpuLoad().cpuHealth.fanRpm
        fun getFanPercent(cpuLoad: CpuLoad) =
            metrics.cpuMetrics().cpuLoad().cpuHealth.fanPercent

        fun getTemperatures(cpuLoad: CpuLoad) =
            metrics.cpuMetrics().cpuLoad().cpuHealth.temperatures
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
        fun getMetrics(drive: Drive) = metrics.driveMetrics().driveLoadByName(drive.name)
    }

    inner class DriveMetricResolver : GraphQLResolver<DriveLoad> {
        fun getDriveId(driveLoad: DriveLoad) = driveLoad.serial
        fun getTemperature(driveLoad: DriveLoad) = driveLoad.health.temperature
        fun getHealthData(driveLoad: DriveLoad) =
            driveLoad.health.healthData.map { DriveHealth(it.data, it.dataType) }

        fun getUsableSpace(driveLoad: DriveLoad) = driveLoad.values.usableSpace
        fun getTotalSpace(driveLoad: DriveLoad) = driveLoad.values.totalSpace
        fun getOpenFileDescriptors(driveLoad: DriveLoad) = driveLoad.values.openFileDescriptors
        fun getMaxFileDescriptors(driveLoad: DriveLoad) = driveLoad.values.maxFileDescriptors
        fun getReads(driveLoad: DriveLoad) = driveLoad.values.reads
        fun getWrites(driveLoad: DriveLoad) = driveLoad.values.writes
        fun getReadBytes(driveLoad: DriveLoad) = driveLoad.values.readBytes
        fun getUsableSpaceBytes(driveLoad: DriveLoad) = driveLoad.values.usableSpace
        fun getTotalSpaceBytes(driveLoad: DriveLoad) = driveLoad.values.totalSpace
        fun getWriteBytes(driveLoad: DriveLoad) = driveLoad.values.writeBytes
        fun getCurrentReadWriteRate(driveLoad: DriveLoad) =
            driveLoad.speed.let {
                DriveReadWriteRate(
                    it.readBytesPerSecond,
                    it.writeBytesPerSecond
                )
            }
    }

    data class DriveHealth(
        val value: Double,
        val type: DataType
    )

    data class DriveReadWriteRate(val readBytesPerSecond: Long, val writeBytesPerSecond: Long)

    inner class NetworkInterfaceResolver : GraphQLResolver<NetworkInterface> {
        fun getId(networkInterface: NetworkInterface) = networkInterface.name
        fun getMetrics(networkInterface: NetworkInterface) =
            metrics.networkMetrics().networkInterfaceLoadById(networkInterface.name)
    }

    inner class NetworkInterfaceMetricResolver : GraphQLResolver<NetworkInterfaceLoad> {
        fun getNetworkInterfaceid(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.name

        fun getBytesReceived(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.bytesReceived

        fun getBytesSent(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.bytesSent

        fun getPacketsReceived(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.packetsReceived

        fun getPacketsSent(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.packetsSent

        fun getInErrors(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.inErrors

        fun getOutErrors(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.values.outErrors

        fun getReadWriteRate(networkInterfaceLoad: NetworkInterfaceLoad) =
            networkInterfaceLoad.speed.let {
                NetworkInterfaceReadWriteRate(
                    it.receiveBytesPerSecond,
                    it.sendBytesPerSecond
                )
            }
    }

    data class NetworkInterfaceReadWriteRate(
        val receiveBytesPerSecond: Long,
        val sendBytesPerSecond: Long
    )

    inner class MemoryLoadResolver : GraphQLResolver<MemoryLoad> {

    }

    inner class MemoryInfoResolver : GraphQLResolver<MemoryInfo> {
        fun metrics(info: MemoryInfo) = metrics.memoryMetrics().memoryLoad()
    }
}