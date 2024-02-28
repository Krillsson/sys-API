package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.cpu.CentralProcessor
import com.krillsson.sysapi.core.domain.cpu.CpuLoad
import com.krillsson.sysapi.core.domain.disk.Disk
import com.krillsson.sysapi.core.domain.disk.DiskLoad
import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.docker.ContainerMetrics
import com.krillsson.sysapi.core.domain.docker.ContainerMetricsHistoryEntry
import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.domain.drives.Drive
import com.krillsson.sysapi.core.domain.drives.DriveLoad
import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.domain.gpu.Gpu
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
import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.core.genericevents.GenericEventRepository
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.docker.ReadLogsCommandResult
import com.krillsson.sysapi.docker.Status
import com.krillsson.sysapi.graphql.domain.*
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsManager
import com.krillsson.sysapi.systemd.SystemDaemonManager
import com.krillsson.sysapi.util.EnvironmentUtils
import com.krillsson.sysapi.util.toOffsetDateTime
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import oshi.hardware.UsbDevice
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

class QueryResolver : GraphQLQueryResolver {

    private lateinit var metrics: Metrics
    private lateinit var monitorManager: MonitorManager
    private lateinit var eventManager: EventManager
    private lateinit var historyRepository: HistoryRepository
    private lateinit var genericEventRepository: GenericEventRepository
    private lateinit var operatingSystem: OperatingSystem
    private lateinit var platform: Platform
    private lateinit var containerManager: ContainerManager
    private lateinit var meta: Meta
    private lateinit var logFilesManager: LogFilesManager
    private lateinit var windowsEventLogManager: WindowsManager
    private lateinit var systemDaemonManager: SystemDaemonManager

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        historyManager: HistoryRepository,
        genericEventRepository: GenericEventRepository,
        containerManager: ContainerManager,
        operatingSystem: OperatingSystem,
        platform: Platform,
        meta: Meta,
        systemDaemonManager: SystemDaemonManager,
        logFilesManager: LogFilesManager,
        windowsManager: WindowsManager
    ) {
        this.metrics = metrics
        this.monitorManager = monitorManager
        this.eventManager = eventManager
        this.genericEventRepository = genericEventRepository
        this.historyRepository = historyManager
        this.operatingSystem = operatingSystem
        this.platform = platform
        this.containerManager = containerManager
        this.meta = meta
        this.systemDaemonManager = systemDaemonManager
        this.windowsEventLogManager = windowsManager
        this.logFilesManager = logFilesManager
    }

    val systemInfoResolver = SystemResolver()
    val historyResolver = HistoryResolver()
    val dockerResolver = DockerResolver()
    val containerResolver = ContainerResolver()
    val pastEventEventResolver = PastEventEventResolver()
    val ongoingEventResolver = OngoingEventResolver()
    val motherboardResolver = MotherboardResolver()
    val processorResolver = ProcessorResolver()
    val processorMetricsResolver = ProcessorMetricsResolver()
    val driveResolver = DriveResolver()
    val diskResolver = DiskResolver()
    val diskMetricResolver = DiskMetricResolver()
    val fileSystemResolver = FileSystemResolver()
    val driveMetricResolver = DriveMetricResolver()
    val networkInterfaceResolver = NetworkInterfaceResolver()
    val memoryLoadResolver = MemoryLoadResolver()
    val memoryInfoResolver = MemoryInfoResolver()
    val networkInterfaceMetricResolver = NetworkInterfaceMetricResolver()
    val monitorResolver = MonitorResolver()
    val updateAvailableGenericEventResolver = UpdateAvailableGenericEventResolver()
    val monitoredItemMissingGenericEventResolver = MonitoredItemMissingGenericEventResolver()

    fun system(): System = System(EnvironmentUtils.hostName, operatingSystem, platform)

    fun history(): List<BasicHistorySystemLoadEntity> {
        return historyRepository.getBasic()
    }

    fun historyBetweenDates(from: OffsetDateTime, to: OffsetDateTime): List<BasicHistorySystemLoadEntity> {
        return historyRepository.getHistoryLimitedToDates(from?.toInstant(), to?.toInstant())
    }

    fun getHistoryBetweenTimestamps(from: Instant, to: Instant): List<BasicHistorySystemLoadEntity> {
        return historyRepository.getHistoryLimitedToDates(from, to)
    }

    fun monitors(): List<com.krillsson.sysapi.graphql.domain.Monitor> {
        return monitorManager.getAll().map { it.asMonitor() }
    }

    fun monitorById(id: String): com.krillsson.sysapi.graphql.domain.Monitor? {
        return monitorManager.getById(UUID.fromString(id))?.asMonitor()
    }


    fun events() = eventManager.getAll().toList()

    fun monitorableItemsForType(input: MonitorableItemsInput): MonitorableItemsOutput {
        val items = monitorManager.getMonitorableItemForType(input.type)

    }

    fun eventById(id: String): Event? {
        return eventManager.getAll().firstOrNull { it.id == UUID.fromString(id) }
    }

    fun genericEvents() = genericEventRepository.read()
    fun pastEvents() = eventManager.getAll().filterIsInstance(PastEvent::class.java)
    fun ongoingEvents() = eventManager.getAll().filterIsInstance(OngoingEvent::class.java)


    fun docker(): Docker {
        return when (val status = containerManager.status) {
            Status.Available -> DockerAvailable
            Status.Disabled -> DockerUnavailable(
                "The docker support is currently disabled. You can change this in configuration.yml",
                isDisabled = true
            )

            is Status.Unavailable -> DockerUnavailable(
                "${status.error.message ?: "Unknown reason"} Type: ${requireNotNull(status.error::class.simpleName)}",
                isDisabled = false
            )
        }
    }

    fun logFiles() = logFilesManager

    fun windowsManagement(): WindowsManagementAccess {
        return if (windowsEventLogManager.supportedBySystem()) {
            windowsEventLogManager
        } else {
            WindowsManagementAccessUnavailable(
                "Not supported by system"
            )
        }
    }

    fun systemDaemon(): SystemDaemonJournalAccess {
        return when (val status = systemDaemonManager.status()) {
            SystemDaemonManager.Status.Available -> systemDaemonManager
            SystemDaemonManager.Status.Disabled -> SystemDaemonAccessUnavailable("Disabled")
            is SystemDaemonManager.Status.Unavailable -> SystemDaemonAccessUnavailable(status.error.message.orEmpty())
        }
    }

    inner class DockerResolver : GraphQLResolver<DockerAvailable> {
        fun containers(docker: DockerAvailable) = containerManager.containers()
        fun container(docker: DockerAvailable, id: String) = containerManager.container(id)
        fun runningContainers(docker: DockerAvailable) =
            containerManager.containers().filter { it.state == State.RUNNING }

        fun readLogsForContainer(
            docker: DockerAvailable,
            containerId: String,
            from: OffsetDateTime?,
            to: OffsetDateTime?
        ): ReadLogsForContainerOutput {
            return when (val result =
                containerManager.readLogsForContainer(containerId, from?.toInstant(), to?.toInstant())) {
                is ReadLogsCommandResult.Success -> ReadLogsForContainerOutputSucceeded(result.lines)
                is ReadLogsCommandResult.Failed -> ReadLogsForContainerOutputFailed(
                    result.error.message ?: result.error.toString()
                )

                is ReadLogsCommandResult.TimedOut -> ReadLogsForContainerOutputFailed("Operation timed out after ${result.timeoutSeconds} seconds")
                ReadLogsCommandResult.Unavailable -> ReadLogsForContainerOutputFailed("Docker is not available")
            }
        }

        fun readLogsForContainerBetweenTimestamps(
            docker: DockerAvailable,
            containerId: String,
            from: Instant?,
            to: Instant?
        ): ReadLogsForContainerOutput {
            return when (val result = containerManager.readLogsForContainer(containerId, from, to)) {
                is ReadLogsCommandResult.Success -> ReadLogsForContainerOutputSucceeded(result.lines)
                is ReadLogsCommandResult.Failed -> ReadLogsForContainerOutputFailed(
                    result.error.message ?: result.error.toString()
                )

                is ReadLogsCommandResult.TimedOut -> ReadLogsForContainerOutputFailed("Operation timed out after ${result.timeoutSeconds} seconds")
                ReadLogsCommandResult.Unavailable -> ReadLogsForContainerOutputFailed("Docker is not available")
            }
        }

        fun metricsForContainer(docker: DockerAvailable, containerId: String): ContainerMetrics? {
            return containerManager.statsForContainer(containerId)
        }

        fun containerMetricsHistoryBetweenTimestamps(
            docker: DockerAvailable,
            containerId: String,
            from: Instant,
            to: Instant
        ): List<ContainerMetricsHistoryEntry> {
            return containerManager.containerMetricsHistoryBetweenTimestamps(
                containerId, from, to
            )
        }
    }

    inner class ContainerResolver : GraphQLResolver<Container> {
        fun metrics(container: Container) = containerManager.statsForContainer(container.id)

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
        fun getDisks(system: System) = metrics.diskMetrics().disks()
        fun getDiskById(system: System, id: String) = metrics.diskMetrics().diskByName(id)
        fun getFileSystems(system: System) = metrics.fileSystemMetrics().fileSystems()
        fun getFileSystemById(system: System, id: String) = metrics.fileSystemMetrics().fileSystemById(id)

        fun getProcesses(
            system: System,
            limit: Int = 0,
            processSortMethod: ProcessSort = ProcessSort.MEMORY
        ): List<Process> {
            return metrics.processesMetrics()
                .processesInfo(processSortMethod, limit).processes
        }

        fun networkInterfaces(system: System) = metrics.networkMetrics().networkInterfaces()
        fun getNetworkInterfaceById(system: System, id: String) = metrics.networkMetrics().networkInterfaceById(id)
        fun getMemory(system: System) = metrics.memoryMetrics().memoryInfo()
    }

    inner class HistoryResolver : GraphQLResolver<BasicHistorySystemLoadEntity> {

        fun getDateTime(historyEntry: BasicHistorySystemLoadEntity): OffsetDateTime {
            return historyEntry.date.toOffsetDateTime()
        }

        fun getTimestamp(historyEntry: BasicHistorySystemLoadEntity): Instant {
            return historyEntry.date
        }

        fun getProcessorMetrics(historyEntry: BasicHistorySystemLoadEntity): CpuLoad {
            return historyRepository.getCpuLoadById(historyEntry.id)
        }

        fun getDriveMetrics(historyEntry: BasicHistorySystemLoadEntity): List<DriveLoad> {
            return historyRepository.getDriveLoadsById(historyEntry.id)
        }

        fun getDiskMetrics(historyEntry: BasicHistorySystemLoadEntity): List<DiskLoad> {
            return historyRepository.getDiskLoadsById(historyEntry.id)
        }

        fun getFileSystemMetrics(historyEntry: BasicHistorySystemLoadEntity): List<FileSystemLoad> {
            return historyRepository.getFileSystemLoadsById(historyEntry.id)
        }

        fun getNetworkInterfaceMetrics(historyEntry: BasicHistorySystemLoadEntity): List<NetworkInterfaceLoad> {
            return historyRepository.getNetworkInterfaceLoadsById(historyEntry.id)
        }

        fun getConnectivity(historyEntry: BasicHistorySystemLoadEntity): Connectivity {
            return historyRepository.getConnectivityById(historyEntry.id)
        }

        fun getMemoryMetrics(historyEntry: BasicHistorySystemLoadEntity): MemoryLoad {
            return historyRepository.getMemoryLoadById(historyEntry.id)
        }
    }

    inner class ProcessorResolver : GraphQLResolver<CentralProcessor> {
        fun getMetrics(processor: CentralProcessor) = metrics.cpuMetrics().cpuLoad()
    }

    inner class ProcessorMetricsResolver : GraphQLResolver<CpuLoad> {
        fun getVoltage(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.voltage.toInt()
        fun getFanRpm(cpuLoad: CpuLoad) = cpuLoad.cpuHealth.fanRpm.toInt()
        fun getFanPercent(cpuLoad: CpuLoad) =
            cpuLoad.cpuHealth.fanPercent.toInt()

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
        diskLoads,
        fileSystemLoads,
        memory,
        emptyList(),
        gpuLoads,
        motherboardHealth
    )

    inner class UpdateAvailableGenericEventResolver : GraphQLResolver<GenericEvent.UpdateAvailable> {
        fun getTitle(event: GenericEvent.UpdateAvailable): String {
            return "sys-API update available"
        }

        fun getDescription(event: GenericEvent.UpdateAvailable): String {
            return "New version ${event.newVersion} published at ${event.publishDate}. Server is running ${event.currentVersion}"
        }

        fun dateTime(event: GenericEvent.UpdateAvailable) = event.timestamp.toOffsetDateTime()
    }

    inner class MonitoredItemMissingGenericEventResolver : GraphQLResolver<GenericEvent.MonitoredItemMissing> {
        fun getTitle(event: GenericEvent.MonitoredItemMissing): String {
            return "Monitored item is missing"
        }

        fun getDescription(event: GenericEvent.MonitoredItemMissing): String {
            return "${event.monitorType.name} monitor's item ${event.monitoredItemId} is no longer present in the system"
        }

        fun dateTime(event: GenericEvent.MonitoredItemMissing) = event.timestamp.toOffsetDateTime()
    }

    inner class MonitorResolver : GraphQLResolver<com.krillsson.sysapi.graphql.domain.Monitor> {

        private fun SystemHistoryEntry.asMonitoredValueHistoryEntry(monitor: com.krillsson.sysapi.graphql.domain.Monitor): MonitoredValueHistoryEntry? {
            val monitoredValue = when (monitor.type.valueType) {
                Monitor.ValueType.Numerical -> Selectors.forNumericalMonitorType(monitor.type)(
                    value.toSystemLoad(),
                    monitor.monitoredItemId
                )

                Monitor.ValueType.Fractional -> Selectors.forFractionalMonitorType(monitor.type)(
                    value.toSystemLoad(),
                    monitor.monitoredItemId
                )

                Monitor.ValueType.Conditional -> Selectors.forConditionalMonitorType(monitor.type)(
                    value.toSystemLoad(),
                    monitor.monitoredItemId
                )
            }
            return monitoredValue?.let { value -> MonitoredValueHistoryEntry(date, value.asMonitoredValue()) }
        }

        fun getHistory(monitor: com.krillsson.sysapi.graphql.domain.Monitor): List<MonitoredValueHistoryEntry> {
            return historyRepository.getExtended().mapNotNull {
                it.asMonitoredValueHistoryEntry(monitor)
            }
        }

        fun getHistoryBetweenTimestamps(
            monitor: com.krillsson.sysapi.graphql.domain.Monitor,
            from: Instant,
            to: Instant
        ): List<MonitoredValueHistoryEntry> {
            return historyRepository.getExtendedHistoryLimitedToDates(from, to).mapNotNull {
                it.asMonitoredValueHistoryEntry(monitor)
            }
        }

        fun events(monitor: com.krillsson.sysapi.graphql.domain.Monitor): List<Event> {
            return eventManager.eventsForMonitorId(monitor.id)
        }

        fun pastEvents(monitor: com.krillsson.sysapi.graphql.domain.Monitor) =
            eventManager.eventsForMonitorId(monitor.id).filterIsInstance(PastEvent::class.java)

        fun ongoingEvents(monitor: com.krillsson.sysapi.graphql.domain.Monitor) =
            eventManager.eventsForMonitorId(monitor.id).filterIsInstance(OngoingEvent::class.java)

        fun getMaxValue(monitor: com.krillsson.sysapi.graphql.domain.Monitor): MonitoredValue? {
            return monitorManager.getById(monitor.id)?.maxValue(metrics.systemMetrics().systemInfo())
                ?.asMonitoredValue()
        }

        fun getCurrentValue(monitor: com.krillsson.sysapi.graphql.domain.Monitor): MonitoredValue? {
            return when (monitor.type) {
                Monitor.Type.CONTAINER_RUNNING -> Selectors.forContainerConditionalMonitor(monitor.type)(
                    containerManager.containersWithIds(listOf(monitor.monitoredItemId.orEmpty())),
                    emptyList(),
                    monitor.monitoredItemId
                )?.asMonitoredValue()

                Monitor.Type.CONTAINER_MEMORY_SPACE -> Selectors.forContainerNumericalMonitor(monitor.type)(
                    emptyList(),
                    listOfNotNull(containerManager.statsForContainer(monitor.monitoredItemId.orEmpty())),
                    monitor.monitoredItemId
                )?.asMonitoredValue()
                Monitor.Type.CONTAINER_CPU_LOAD -> Selectors.forContainerFractionalMonitor(monitor.type)(
                    emptyList(),
                    listOfNotNull(containerManager.statsForContainer(monitor.monitoredItemId.orEmpty())),
                    monitor.monitoredItemId
                )?.asMonitoredValue()
                else -> when (monitor.type.valueType) {
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
                }?.asMonitoredValue()
            }
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

        fun startTimestamp(event: OngoingEvent): Instant {
            return event.startTime
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

        fun getStartValue(event: PastEvent): MonitoredValue {
            return event.startValue.asMonitoredValue()
        }

        fun startTimestamp(event: PastEvent): Instant {
            return event.startTime
        }

        fun endTimestamp(event: PastEvent): Instant {
            return event.endTime
        }
    }

    inner class MotherboardResolver : GraphQLResolver<Motherboard> {
        fun getManufacturer(motherboard: Motherboard) = motherboard.computerSystem.manufacturer
        fun getModel(motherboard: Motherboard) = motherboard.computerSystem.model
        fun getSerialNumber(motherboard: Motherboard) = motherboard.computerSystem.serialNumber
        fun getFirmware(motherboard: Motherboard) = motherboard.computerSystem.firmware
    }

    inner class DiskResolver : GraphQLResolver<Disk> {
        fun getId(drive: Disk) = drive.serial
        fun getMetrics(drive: Disk) = metrics.diskMetrics().diskLoadByName(drive.name)
    }

    inner class DiskMetricResolver : GraphQLResolver<DiskLoad> {
        fun getDiskId(driveLoad: DiskLoad) = driveLoad.serial
        fun getReads(driveLoad: DiskLoad) = driveLoad.values.reads
        fun getWrites(driveLoad: DiskLoad) = driveLoad.values.writes
        fun getReadBytes(driveLoad: DiskLoad) = driveLoad.values.readBytes
        fun getWriteBytes(driveLoad: DiskLoad) = driveLoad.values.writeBytes
        fun getCurrentReadWriteRate(driveLoad: DiskLoad) =
            driveLoad.speed
    }

    inner class FileSystemResolver : GraphQLResolver<com.krillsson.sysapi.core.domain.filesystem.FileSystem> {
        fun getMetrics(fileSystem: com.krillsson.sysapi.core.domain.filesystem.FileSystem) =
            metrics.fileSystemMetrics().fileSystemLoadById(fileSystem.id)
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

    inner class DriveResolver : GraphQLResolver<Drive> {
        fun getId(drive: Drive) = drive.serial
        fun getMetrics(drive: Drive) = metrics.driveMetrics().driveLoadByName(drive.name)
    }

    data class DriveHealth(
        val value: Double,
        val type: DataType
    )

    data class DriveReadWriteRate(val readBytesPerSecond: Long, val writeBytesPerSecond: Long)

    inner class NetworkInterfaceResolver : GraphQLResolver<NetworkInterface> {
        fun getId(networkInterface: NetworkInterface) = networkInterface.name

        fun getSpeed(networkInterface: NetworkInterface) = networkInterface.speedBitsPerSeconds
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