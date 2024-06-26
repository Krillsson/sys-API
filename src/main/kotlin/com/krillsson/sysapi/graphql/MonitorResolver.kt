package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.core.webservicecheck.WebServerCheckService
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.graphql.domain.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.time.Instant
import java.util.*

@Controller
@SchemaMapping(typeName="Monitor")
class MonitorResolver(
        val historyRepository: HistoryRepository,
        val eventManager: EventManager,
        val monitorManager: MonitorManager,
        val containerManager: ContainerManager,
        val webServerCheckService: WebServerCheckService,
        val metrics: Metrics
) {

    private fun SystemHistoryEntry.asMonitoredValueHistoryEntry(monitor: Monitor): MonitoredValueHistoryEntry? {
        val monitoredValue = when (monitor.type.valueType) {
            com.krillsson.sysapi.core.monitoring.Monitor.ValueType.Numerical -> Selectors.forNumericalMonitorType(
                    monitor.type
            )(
                    value.toSystemLoad(),
                    monitor.monitoredItemId
            )

            com.krillsson.sysapi.core.monitoring.Monitor.ValueType.Fractional -> Selectors.forFractionalMonitorType(
                    monitor.type
            )(
                    value.toSystemLoad(),
                    monitor.monitoredItemId
            )

            com.krillsson.sysapi.core.monitoring.Monitor.ValueType.Conditional -> Selectors.forConditionalMonitorType(
                    monitor.type
            )(
                    value.toSystemLoad(),
                    monitor.monitoredItemId
            )
        }
        return monitoredValue?.let { value -> MonitoredValueHistoryEntry(date, value.asMonitoredValue()) }
    }

    @SchemaMapping
    fun history(monitor: Monitor): List<MonitoredValueHistoryEntry> {
        return historyRepository.getExtended().mapNotNull {
            it.asMonitoredValueHistoryEntry(monitor)
        }
    }

    @SchemaMapping
    fun historyBetweenTimestamps(
            monitor: Monitor,
            @Argument from: Instant,
            @Argument to: Instant
    ): List<MonitoredValueHistoryEntry> {
        return historyRepository.getExtendedHistoryLimitedToDates(from, to).mapNotNull {
            it.asMonitoredValueHistoryEntry(monitor)
        }
    }
    @SchemaMapping
    fun events(monitor: Monitor): List<Event> {
        return eventManager.eventsForMonitorId(monitor.id)
    }
    @SchemaMapping
    fun pastEvents(monitor: Monitor) =
            eventManager.eventsForMonitorId(monitor.id).filterIsInstance(PastEvent::class.java)
    @SchemaMapping
    fun ongoingEvents(monitor: Monitor) =
            eventManager.eventsForMonitorId(monitor.id).filterIsInstance(OngoingEvent::class.java)
    @SchemaMapping
    fun maxValue(monitor: Monitor): MonitoredValue? {
        return monitorManager.getById(monitor.id)?.maxValue(metrics.systemMetrics().systemInfo())
                ?.asMonitoredValue()
    }
    @SchemaMapping
    fun currentValue(monitor: Monitor): MonitoredValue? {
        return when (monitor.type) {
            com.krillsson.sysapi.core.monitoring.Monitor.Type.CONTAINER_RUNNING -> Selectors.forContainerConditionalMonitor(
                    monitor.type
            )(
                    containerManager.containersWithIds(listOf(monitor.monitoredItemId.orEmpty())),
                    emptyList(),
                    monitor.monitoredItemId
            )?.asMonitoredValue()

            com.krillsson.sysapi.core.monitoring.Monitor.Type.CONTAINER_MEMORY_SPACE -> Selectors.forContainerNumericalMonitor(
                    monitor.type
            )(
                    emptyList(),
                    listOfNotNull(containerManager.statsForContainer(monitor.monitoredItemId.orEmpty())),
                    monitor.monitoredItemId
            )?.asMonitoredValue()

            com.krillsson.sysapi.core.monitoring.Monitor.Type.CONTAINER_CPU_LOAD -> Selectors.forContainerFractionalMonitor(
                    monitor.type
            )(
                    emptyList(),
                    listOfNotNull(containerManager.statsForContainer(monitor.monitoredItemId.orEmpty())),
                    monitor.monitoredItemId
            )?.asMonitoredValue()
            com.krillsson.sysapi.core.monitoring.Monitor.Type.WEBSERVER_UP -> {
                (webServerCheckService.getStatusForWebServer(UUID.fromString(monitor.monitoredItemId))?.responseCode == 200).toConditionalValue().asMonitoredValue()
            }

            else -> when (monitor.type.valueType) {
                com.krillsson.sysapi.core.monitoring.Monitor.ValueType.Numerical -> Selectors.forNumericalMonitorType(
                        monitor.type
                )(
                        metrics.systemMetrics().systemLoad(),
                        monitor.monitoredItemId
                )

                com.krillsson.sysapi.core.monitoring.Monitor.ValueType.Fractional -> Selectors.forFractionalMonitorType(
                        monitor.type
                )(
                        metrics.systemMetrics().systemLoad(),
                        monitor.monitoredItemId
                )

                com.krillsson.sysapi.core.monitoring.Monitor.ValueType.Conditional -> Selectors.forConditionalMonitorType(
                        monitor.type
                )(
                        metrics.systemMetrics().systemLoad(),
                        monitor.monitoredItemId
                )
            }?.asMonitoredValue()
        }
    }

    private fun HistorySystemLoad.toSystemLoad() = SystemLoad(
            uptime,
            systemLoadAverage,
            cpuLoad,
            networkInterfaceLoads,
            connectivity,
            diskLoads,
            fileSystemLoads,
            memory,
            emptyList(),
            gpuLoads,
            motherboardHealth
    )
}