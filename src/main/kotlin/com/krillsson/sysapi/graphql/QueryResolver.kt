package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.genericevents.GenericEventRepository
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.history.db.BasicHistorySystemLoadEntity
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.docker.Status
import com.krillsson.sysapi.graphql.domain.*
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsManager
import com.krillsson.sysapi.systemd.SystemDaemonManager
import com.krillsson.sysapi.util.EnvironmentUtils
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

@Component
class QueryResolver(
    private val metrics: Metrics,
    private val monitorManager: MonitorManager,
    private val eventManager: EventManager,
    private val historyRepository: HistoryRepository,
    private val genericEventRepository: GenericEventRepository,
    private val operatingSystem: OperatingSystem,
    private val platform: Platform,
    private val containerManager: ContainerManager,
    private val meta: Meta,
    private val logFilesManager: LogFilesManager,
    private val windowsEventLogManager: WindowsManager,
    private val systemDaemonManager: SystemDaemonManager,
) : GraphQLQueryResolver {

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
        val items = monitorManager.getMonitorableItemForType(input.type).map {
            MonitorableItem(
                it.id,
                it.name,
                it.description,
                it.maxValue.asMonitoredValue(),
                it.currentValue.asMonitoredValue(),
                it.type
            )
        }
        return MonitorableItemsOutput(items)
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
}