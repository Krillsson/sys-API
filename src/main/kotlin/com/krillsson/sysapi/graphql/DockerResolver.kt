package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.docker.ContainerMetrics
import com.krillsson.sysapi.core.domain.docker.ContainerMetricsHistoryEntry
import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.docker.ReadLogsCommandResult
import com.krillsson.sysapi.graphql.domain.DockerAvailable
import com.krillsson.sysapi.graphql.domain.ReadLogsForContainerOutput
import com.krillsson.sysapi.graphql.domain.ReadLogsForContainerOutputFailed
import com.krillsson.sysapi.graphql.domain.ReadLogsForContainerOutputSucceeded
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.OffsetDateTime

@Component
class DockerResolver(val containerManager: ContainerManager) : GraphQLResolver<DockerAvailable> {
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