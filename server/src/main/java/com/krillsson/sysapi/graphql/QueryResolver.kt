package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.domain.gpu.Gpu
import com.krillsson.sysapi.core.domain.gpu.GpuLoad
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.memory.MemoryInfo
import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import com.krillsson.sysapi.core.domain.motherboard.Motherboard
import com.krillsson.sysapi.core.domain.network.Connectivity
import com.krillsson.sysapi.core.domain.network.NetworkInterface
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad
import com.krillsson.sysapi.core.domain.processes.Process
import com.krillsson.sysapi.core.domain.processes.ProcessSort
import com.krillsson.sysapi.core.domain.sensors.DataType
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.graphql.domain.*
import com.krillsson.sysapi.util.EnvironmentUtils
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import oshi.hardware.UsbDevice
import java.time.OffsetDateTime

class QueryResolver : GraphQLQueryResolver {

    lateinit var metrics: Metrics
    lateinit var monitorManager: MonitorManager
    lateinit var eventManager: EventManager
    lateinit var historyManager: HistoryManager
    lateinit var operatingSystem: OperatingSystem
    lateinit var platform: Platform
    lateinit var dockerClient: DockerClient
    lateinit var meta: Meta

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        historyManager: HistoryManager,
        dockerClient: DockerClient,
        operatingSystem: OperatingSystem,
        platform: Platform,
        meta: Meta
    ) {
        this.metrics = metrics
        this.monitorManager = monitorManager
        this.eventManager = eventManager
        this.historyManager = historyManager
        this.operatingSystem = operatingSystem
        this.platform = platform
        this.dockerClient = dockerClient
        this.meta = meta
    }

    val systemInfoResolver = SystemResolver()
    val historyResolver = HistoryResolver()
    val dockerResolver = DockerResolver()
    val pastEventEventResolver = PastEventEventResolver()
    val ongoingEventResolver = OngoingEventResolver()
    val motherboardResolver = MotherboardResolver()
    val processorResolver = ProcessorResolver()
    val processorMetricsResolver = ProcessorMetricsResolver()
    val driveResolver = DriveResolver()
    val driveMetricResolver = DriveMetricResolver()
    val networkInterfaceResolver = NetworkInterfaceResolver()
    val memoryLoadResolver = MemoryLoadResolver()
    val memoryInfoResolver = MemoryInfoResolver()
    val networkInterfaceMetricResolver = NetworkInterfaceMetricResolver()
    val monitorResolver = MonitorResolver()

    fun system(): System = System(EnvironmentUtils.hostName, operatingSystem, platform)

    fun history(): List<SystemHistoryEntry> {
        return historyManager.getHistory().map {
            SystemHistoryEntry(
                it.date,
                it.value
            )
        }.toList()
    }

    fun historyBetweenDates(from: OffsetDateTime, to: OffsetDateTime): List<SystemHistoryEntry> {
        return historyManager.getHistoryLimitedToDates(from, to).map {
            SystemHistoryEntry(
                it.date,
                it.value
            )
        }.toList()
    }

    fun monitors(): List<com.krillsson.sysapi.graphql.domain.Monitor> {
        return monitorManager.getAll().map { it.asMonitor() }
    }

    fun events() = eventManager.getAll().toList()
    fun pastEvents() = eventManager.getAll().filterIsInstance(PastEvent::class.java)
    fun ongoingEvents() = eventManager.getAll().filterIsInstance(OngoingEvent::class.java)


    fun docker(): Docker {
        return when (val status = dockerClient.status) {
            DockerClient.Status.Available -> DockerAvailable
            DockerClient.Status.Disabled -> DockerUnavailable(
                "The docker support is currently disabled. You can change this in configuration.yml",
                isDisabled = true
            )

            is DockerClient.Status.Unavailable -> DockerUnavailable(
                "${status.error.message ?: "Unknown reason"} Type: ${requireNotNull(status.error::class.simpleName)}",
                isDisabled = false
            )
        }
    }

    inner class DockerResolver : GraphQLResolver<DockerAvailable> {
        fun containers(docker: DockerAvailable) = dockerClient.listContainers()
        fun runningContainers(docker: DockerAvailable) =
            dockerClient.listContainers().filter { it.state == State.RUNNING }

        fun readLogsForContainer(
            docker: DockerAvailable,
            containerId: String,
            from: OffsetDateTime?,
            to: OffsetDateTime?
        ): ReadLogsForContainerOutput {
            return when (val result = dockerClient.readLogsForContainer(containerId, from, to)) {
                is DockerClient.ReadLogsCommandResult.Success -> ReadLogsForContainerOutputSucceeded(result.lines)
                is DockerClient.ReadLogsCommandResult.Failed -> ReadLogsForContainerOutputFailed(
                    result.error.message ?: result.error.toString()
                )

                is DockerClient.ReadLogsCommandResult.TimedOut -> ReadLogsForContainerOutputFailed("Operation timed out after ${result.timeoutSeconds} seconds")
                DockerClient.ReadLogsCommandResult.Unavailable -> ReadLogsForContainerOutputFailed("Docker is not available")
            }
        }
    }

    inner class SystemResolver : GraphQLResolver<System> {
        fun processorMetrics(system: System) = metrics.cpuMetrics().cpuLoad()

        fun getBaseboard(system: System): Motherboard {
            return metrics.motherboardMetrics().motherboard()
        }

        fun getHostname(system: System): String {
            return system.hostName
        }

        fun getUsbDevices(system: System): List<UsbDevice> {
            return getBaseboard(system).usbDevices.toList()
        }

        fun getUptime(system: System): Long {
            return metrics.cpuMetrics().uptime()
        }

        fun getConnectivity(system: System): Connectivity {
            return metrics.networkMetrics().connectivity()
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
        ): List<Process> {
            return metrics.processesMetrics()
                .processesInfo(processSortMethod, limit).processes
        }

        fun networkInterfaces(system: System) = metrics.networkMetrics().networkInterfaces()
        fun getMemory(system: System) = metrics.memoryMetrics().memoryInfo()
    }

    inner class HistoryResolver : GraphQLResolver<SystemHistoryEntry> {

        fun getDateTime(historyEntry: SystemHistoryEntry): OffsetDateTime {
            return historyEntry.date
        }

        fun getProcessorMetrics(historyEntry: SystemHistoryEntry): CpuLoad {
            return historyEntry.value.cpuLoad
        }

        fun getDriveMetrics(historyEntry: SystemHistoryEntry): List<DriveLoad> {
            return historyEntry.value.driveLoads
        }

        fun getNetworkInterfaceMetrics(historyEntry: SystemHistoryEntry): List<NetworkInterfaceLoad> {
            return historyEntry.value.networkInterfaceLoads
        }

        fun getConnectivity(historyEntry: SystemHistoryEntry): Connectivity {
            return historyEntry.value.connectivity
        }

        fun getGraphicsMetrics(historyEntry: SystemHistoryEntry): List<GpuLoad> {
            return historyEntry.value.gpuLoads
        }

        fun getMemoryMetrics(historyEntry: SystemHistoryEntry): MemoryLoad {
            return historyEntry.value.memory
        }
    }

    inner class ProcessorResolver : GraphQLResolver<CentralProcessor> {
        fun getMetrics(processor: CentralProcessor) = metrics.cpuMetrics().cpuLoad()
    }

    inner class ProcessorMetricsResolver : GraphQLResolver<CpuLoad> {
        fun getVoltage(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.voltage
        fun getFanRpm(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.fanRpm
        fun getFanPercent(cpuLoad: CpuLoad) =
            cpuLoad.cpuHealth.fanPercent

        fun getTemperatures(cpuLoad: CpuLoad) =
            cpuLoad.cpuHealth.temperatures
    }

    private fun HistorySystemLoad.toSystemLoad() = SystemLoad(
        uptime,
        systemLoadAverage,
        cpuLoad,
        networkInterfaceLoads,
        connectivity,
        driveLoads,
        memory,
        emptyList(),
        gpuLoads,
        motherboardHealth
    )

    inner class MonitorResolver : GraphQLResolver<com.krillsson.sysapi.graphql.domain.Monitor> {
        fun getHistory(monitor: com.krillsson.sysapi.graphql.domain.Monitor): List<MonitoredValueHistoryEntry> {
            return historyManager.getHistory().mapNotNull {
                val monitoredValue = when (monitor.type.valueType) {
                    Monitor.ValueType.Numerical -> Selectors.forNumericalMonitorType(monitor.type)(
                        it.value.toSystemLoad(),
                        monitor.monitoredItemId
                    )

                    Monitor.ValueType.Fractional -> Selectors.forFractionalMonitorType(monitor.type)(
                        it.value.toSystemLoad(),
                        monitor.monitoredItemId
                    )

                    Monitor.ValueType.Conditional -> Selectors.forConditionalMonitorType(monitor.type)(
                        it.value.toSystemLoad(),
                        monitor.monitoredItemId
                    )
                }
                monitoredValue?.let { value -> MonitoredValueHistoryEntry(it.date, value.asMonitoredValue()) }
            }
        }

        fun getCurrentValue(monitor: com.krillsson.sysapi.graphql.domain.Monitor): MonitoredValue {
            metrics.systemMetrics().systemLoad()
            return requireNotNull(
                when (monitor.type.valueType) {
                    Monitor.ValueType.Numerical -> Selectors.forNumericalMonitorType(monitor.type)(
                        metrics.systemMetrics().systemLoad(),
                        monitor.monitoredItemId
                    )

                    Monitor.ValueType.Fractional -> Selectors.forFractionalMonitorType(monitor.type)(
                        metrics.systemMetrics().systemLoad(),
                        monitor.monitoredItemId
                    )

                    Monitor.ValueType.Conditional -> Selectors.forConditionalMonitorType(monitor.type)(
                        metrics.systemMetrics().systemLoad(),
                        monitor.monitoredItemId
                    )
                }
            ) { "No value found for monitor of type ${monitor.type} with id ${monitor.id}" }.asMonitoredValue()
        }
    }

    inner class MonitorEventResolver : GraphQLResolver<MonitorEvent> {
        fun getType(monitorEvent: MonitorEvent) = monitorEvent.monitorType
    }

    inner class OngoingEventResolver : GraphQLResolver<OngoingEvent> {
        fun getMonitor(event: OngoingEvent): com.krillsson.sysapi.graphql.domain.Monitor {
            return requireNotNull(
                monitorManager.getById(event.monitorId)?.asMonitor()
            ) { "No monitor of type ${event.monitorType} with id ${event.monitorId}" }
        }

        fun getStartValue(event: OngoingEvent): MonitoredValue {
            return event.value.asMonitoredValue()
        }
    }

    inner class PastEventEventResolver : GraphQLResolver<PastEvent> {
        fun getMonitor(event: PastEvent): com.krillsson.sysapi.graphql.domain.Monitor {
            return requireNotNull(
                monitorManager.getById(event.monitorId)?.asMonitor()
            ) { "No monitor of type ${event.monitorType} with id ${event.monitorId}" }
        }

        fun getEndValue(event: PastEvent): MonitoredValue {
            return event.value.asMonitoredValue()
        }
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