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
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.time.Instant
import java.time.OffsetDateTime

@Controller
class DockerResolver(val containerManager: ContainerManager) {

    @SchemaMapping
    fun containers(docker: DockerAvailable) = containerManager.containers()
    @SchemaMapping
    fun container(docker: DockerAvailable, @Argument id: String) = containerManager.container(id)
    @SchemaMapping
    fun runningContainers(docker: DockerAvailable) =
            containerManager.containers().filter { it.state == State.RUNNING }

    fun readLogsForContainer(
            docker: DockerAvailable,
            @Argument
            containerId: String,
            @Argument
            from: OffsetDateTime?,
            @Argument
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

    @SchemaMapping
    fun readLogsForContainerBetweenTimestamps(
            docker: DockerAvailable,
            @Argument
            containerId: String,
            @Argument
            from: Instant?,
            @Argument
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

    @SchemaMapping
    fun metricsForContainer(docker: DockerAvailable, @Argument containerId: String): ContainerMetrics? {
        return containerManager.statsForContainer(containerId)
    }

    @SchemaMapping
    fun containerMetricsHistoryBetweenTimestamps(
            docker: DockerAvailable,
            @Argument containerId: String,
            @Argument from: Instant,
            @Argument to: Instant
    ): List<ContainerMetricsHistoryEntry> {
        return containerManager.containerMetricsHistoryBetweenTimestamps(
                containerId, from, to
        )
    }
}