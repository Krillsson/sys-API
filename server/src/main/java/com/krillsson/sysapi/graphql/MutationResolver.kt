package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.docker.Command
import com.krillsson.sysapi.core.domain.monitor.toConditionalValue
import com.krillsson.sysapi.core.domain.monitor.toFractionalValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.genericevents.GenericEventRepository
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.graphql.mutations.*
import com.krillsson.sysapi.systemd.SystemDaemonJournalManager
import graphql.kickstart.tools.GraphQLMutationResolver
import java.time.Duration

class MutationResolver : GraphQLMutationResolver {

    lateinit var metrics: Metrics
    lateinit var monitorManager: MonitorManager
    lateinit var eventManager: EventManager
    lateinit var genericEventRepository: GenericEventRepository
    lateinit var dockerClient: DockerClient
    lateinit var systemDaemonJournalManager: SystemDaemonJournalManager

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        genericEventRepository: GenericEventRepository,
        eventManager: EventManager,
        dockerClient: DockerClient,
        systemDaemonJournalManager: SystemDaemonJournalManager
    ) {
        this.metrics = metrics
        this.monitorManager = monitorManager
        this.eventManager = eventManager
        this.dockerClient = dockerClient
        this.genericEventRepository = genericEventRepository
        this.systemDaemonJournalManager = systemDaemonJournalManager
    }

    fun performDockerContainerCommand(input: PerformDockerContainerCommandInput): PerformDockerContainerCommandOutput {
        val result = dockerClient.performCommandWithContainer(
            Command(input.containerId, input.command)
        )

        return when (result) {
            is DockerClient.CommandResult.Failed -> PerformDockerContainerCommandOutputFailed(
                "Message: ${result.error.message ?: "Unknown reason"} Type: ${requireNotNull(result.error::class.simpleName)}",
            )

            DockerClient.CommandResult.Success -> PerformDockerContainerCommandOutputSucceeded(input.containerId)
            DockerClient.CommandResult.Unavailable -> PerformDockerContainerCommandOutputFailed("Docker client is unavailable")
        }
    }

    fun performSystemDaemonCommand(input: PerformSystemDaemonCommandInput): PerformSystemDaemonCommandOutput {
        val result = systemDaemonJournalManager.performCommandWithService(
            input.serviceName, input.command
        )

        return when (result) {
            is SystemDaemonJournalManager.CommandResult.Failed -> PerformSystemDaemonCommandOutputFailed(
                "Message: ${result.error.message ?: "Unknown reason"} Type: ${requireNotNull(result.error::class.simpleName)}",
            )

            SystemDaemonJournalManager.CommandResult.Success -> PerformSystemDaemonCommandOutputSucceeded(input.serviceName)
            SystemDaemonJournalManager.CommandResult.Unavailable -> PerformSystemDaemonCommandOutputFailed("SystemDaemon is unavailable")
        }
    }

    fun createNumericalValueMonitor(input: CreateNumericalMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toNumericalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun createFractionalValueMonitor(input: CreateFractionMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toFractionalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun createConditionalValueMonitor(input: CreateConditionalMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toConditionalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun deleteMonitor(input: DeleteMonitorInput): DeleteMonitorOutput {
        val removed = monitorManager.remove(input.monitorId)
        return DeleteMonitorOutput(removed)
    }

    fun updateNumericalValueMonitor(input: UpdateNumericalMonitorInput): UpdateMonitorOutput {
        return try {
            val updatedMonitorId = monitorManager.update(
                input.monitorId,
                input.inertiaInSeconds?.toLong()?.let { Duration.ofSeconds(it) },
                input.threshold?.toNumericalValue()
            )
            UpdateMonitorOutputSucceeded(updatedMonitorId)
        } catch (exception: Exception) {
            UpdateMonitorOutputFailed(exception.message ?: "Unknown reason")
        }
    }

    fun updateFractionalValueMonitor(input: UpdateFractionMonitorInput): UpdateMonitorOutput {
        return try {
            val updatedMonitorId = monitorManager.update(
                input.monitorId,
                input.inertiaInSeconds?.toLong()?.let { Duration.ofSeconds(it) },
                input.threshold?.toFractionalValue()
            )
            UpdateMonitorOutputSucceeded(updatedMonitorId)
        } catch (exception: Exception) {
            UpdateMonitorOutputFailed(exception.message ?: "Unknown reason")
        }
    }

    fun updateConditionalValueMonitor(input: UpdateConditionalMonitorInput): UpdateMonitorOutput {
        return try {
            val updatedMonitorId = monitorManager.update(
                input.monitorId,
                input.inertiaInSeconds?.toLong()?.let { Duration.ofSeconds(it) },
                input.threshold?.toConditionalValue()
            )
            UpdateMonitorOutputSucceeded(updatedMonitorId)
        } catch (exception: Exception) {
            UpdateMonitorOutputFailed(exception.message ?: "Unknown reason")
        }
    }

    fun deleteEvent(input: DeleteEventInput): DeleteEventOutput {
        val removed = eventManager.remove(input.eventId)
        return DeleteEventOutput(removed)
    }

    fun deleteGenericEvent(input: DeleteGenericEventInput): DeleteGenericEventOutput {
        val removed = genericEventRepository.removeById(input.eventId)
        return DeleteGenericEventOutput(removed)
    }

    fun deleteEventsForMonitor(input: DeleteEventsForMonitorInput): DeleteEventOutput {
        val removed = eventManager.removeEventsForMonitorId(input.monitorId)
        return DeleteEventOutput(removed)
    }
}