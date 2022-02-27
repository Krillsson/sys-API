package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.docker.Command
import com.krillsson.sysapi.core.domain.monitor.toBooleanValue
import com.krillsson.sysapi.core.domain.monitor.toFractionalValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.graphql.mutations.*
import graphql.kickstart.tools.GraphQLMutationResolver
import java.time.Duration

class MutationResolver : GraphQLMutationResolver {

    lateinit var metrics: Metrics
    lateinit var monitorManager: MonitorManager
    lateinit var eventManager: EventManager
    lateinit var historyManager: HistoryManager
    lateinit var dockerClient: DockerClient

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        historyManager: HistoryManager,
        dockerClient: DockerClient
    ) {
        this.metrics = metrics
        this.monitorManager = monitorManager
        this.eventManager = eventManager
        this.historyManager = historyManager
        this.dockerClient = dockerClient
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

    fun createNumericalMonitor(input: CreateNumericalMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type.toMonitorType(),
            input.threshold.toNumericalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun createFractionalMonitor(input: CreateFractionMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type.toMonitorType(),
            input.threshold.toFractionalValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun createBooleanMonitor(input: CreateBooleanMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type.toMonitorType(),
            input.threshold.toBooleanValue(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun deleteMonitor(input: DeleteMonitorInput): DeleteMonitorOutput {
        val removed = monitorManager.remove(input.monitorId)
        return DeleteMonitorOutput(removed)
    }

    fun updateNumericalMonitor(input: UpdateNumericalMonitorInput): UpdateMonitorOutput {
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

    fun updateFractionalMonitor(input: UpdateFractionMonitorInput): UpdateMonitorOutput {
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

    fun updateBooleanMonitor(input: UpdateBooleanMonitorInput): UpdateMonitorOutput {
        return try {
            val updatedMonitorId = monitorManager.update(
                input.monitorId,
                input.inertiaInSeconds?.toLong()?.let { Duration.ofSeconds(it) },
                input.threshold?.toBooleanValue()
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

    fun deleteEventsForMonitor(input: DeleteEventsForMonitorInput): DeleteEventOutput {
        val removed = eventManager.removeEventsForMonitorId(input.monitorId)
        return DeleteEventOutput(removed)
    }
}