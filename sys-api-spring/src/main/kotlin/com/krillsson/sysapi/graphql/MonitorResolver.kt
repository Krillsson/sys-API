package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.graphql.domain.Monitor
import com.krillsson.sysapi.graphql.domain.MonitoredValue
import com.krillsson.sysapi.graphql.domain.MonitoredValueHistoryEntry
import com.krillsson.sysapi.graphql.domain.Selectors
import com.krillsson.sysapi.graphql.domain.asMonitoredValue
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class MonitorResolver(
    val historyRepository: HistoryRepository,
    val eventManager: EventManager,
    val monitorManager: MonitorManager,
    val containerManager: ContainerManager,
    val metrics: Metrics
) : GraphQLResolver<Monitor> {

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

    fun getHistory(monitor: Monitor): List<MonitoredValueHistoryEntry> {
        return historyRepository.getExtended().mapNotNull {
            it.asMonitoredValueHistoryEntry(monitor)
        }
    }

    fun getHistoryBetweenTimestamps(
        monitor: Monitor,
        from: Instant,
        to: Instant
    ): List<MonitoredValueHistoryEntry> {
        return historyRepository.getExtendedHistoryLimitedToDates(from, to).mapNotNull {
            it.asMonitoredValueHistoryEntry(monitor)
        }
    }

    fun events(monitor: Monitor): List<Event> {
        return eventManager.eventsForMonitorId(monitor.id)
    }

    fun pastEvents(monitor: Monitor) =
        eventManager.eventsForMonitorId(monitor.id).filterIsInstance(PastEvent::class.java)

    fun ongoingEvents(monitor: Monitor) =
        eventManager.eventsForMonitorId(monitor.id).filterIsInstance(OngoingEvent::class.java)

    fun getMaxValue(monitor: Monitor): MonitoredValue? {
        return monitorManager.getById(monitor.id)?.maxValue(metrics.systemMetrics().systemInfo())
            ?.asMonitoredValue()
    }

    fun getCurrentValue(monitor: Monitor): MonitoredValue? {
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