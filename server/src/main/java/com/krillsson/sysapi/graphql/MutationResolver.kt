package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.docker.Command
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

    fun createMonitor(input: CreateMonitorInput): CreateMonitorOutput {
        val createdId = monitorManager.add(
            Duration.ofSeconds(input.inertiaInSeconds.toLong()),
            input.type,
            input.threshold.toDouble(),
            input.monitoredItemId
        )
        return CreateMonitorOutput(createdId)
    }

    fun deleteMonitor(input: DeleteMonitorInput): DeleteMonitorOutput {
        val removed = monitorManager.remove(input.monitorId)
        return DeleteMonitorOutput(removed)
    }

    fun deleteEvent(input: DeleteEventInput): DeleteEventOutput {
        val removed = eventManager.remove(input.eventId)
        return DeleteEventOutput(removed)
    }

    fun deleteEventsForMonitor(input: DeleteMonitorInput): DeleteEventOutput {
        val removed = eventManager.removeEventsForMonitorId(input.monitorId)
        return DeleteEventOutput(removed)
    }
}