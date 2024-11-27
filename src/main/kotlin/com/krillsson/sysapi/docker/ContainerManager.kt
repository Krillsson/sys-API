package com.krillsson.sysapi.docker

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.krillsson.sysapi.config.DockerConfiguration
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.domain.docker.Command
import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.docker.ContainerMetrics
import com.krillsson.sysapi.core.domain.docker.ContainerMetricsHistoryEntry
import com.krillsson.sysapi.core.history.ContainersHistoryRepository
import com.krillsson.sysapi.graphql.domain.DockerLogMessageConnection
import com.krillsson.sysapi.graphql.domain.DockerLogMessageEdge
import com.krillsson.sysapi.graphql.domain.PageInfo
import com.krillsson.sysapi.util.decodeAsInstantCursor
import com.krillsson.sysapi.util.encodeAsCursor
import com.krillsson.sysapi.util.logger
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ContainerManager(
    private val dockerClient: DockerClient,
    private val config: YAMLConfigFile,
    private val containersHistoryRepository: ContainersHistoryRepository,
) {
    private val logger by logger()

    private val dockerConfiguration: DockerConfiguration = config.docker
    private val cacheConfiguration = config.docker.cache
    val status = checkAvailability()


    private val containersCache: Supplier<List<Container>> = Suppliers.memoizeWithExpiration(
        {
            if (status == Status.Available) {
                dockerClient.listContainers()
            } else {
                emptyList()
            }
        },
        cacheConfiguration.duration, cacheConfiguration.unit
    )

    private val containerStatsCache: LoadingCache<String, Optional<ContainerMetrics>> = CacheBuilder.newBuilder()
        .expireAfterWrite(cacheConfiguration.duration, cacheConfiguration.unit)
        .build(object : CacheLoader<String, Optional<ContainerMetrics>>() {
            @Throws(Exception::class)
            override fun load(id: String): Optional<ContainerMetrics> {
                return if (status == Status.Available) {
                    dockerClient.containerStatistics(id)?.let { Optional.of(it) } ?: Optional.empty()
                } else {
                    Optional.empty()
                }
            }
        })

    fun containers(): List<Container> {
        return when {
            status != Status.Available -> emptyList()
            !cacheConfiguration.enabled -> dockerClient.listContainers()
            else -> containersCache.get()
        }
    }

    fun container(id: String): Container? {
        return containersWithIds(listOf(id)).firstOrNull()
    }

    fun containersWithIds(ids: List<String>): List<Container> {
        return when {
            status != Status.Available -> emptyList()
            !cacheConfiguration.enabled -> dockerClient.listContainers(ids)
            else -> containersCache.get().filter { ids.contains(it.id) }
        }
    }

    fun containerMetricsHistoryBetweenTimestamps(
        containerId: String,
        from: Instant,
        to: Instant
    ): List<ContainerMetricsHistoryEntry> {
        return containersHistoryRepository.getHistoryLimitedToDates(
            containerId,
            from,
            to
        )
    }

    fun statsForContainer(id: String): ContainerMetrics? {
        return when {
            status != Status.Available -> null
            !cacheConfiguration.enabled -> dockerClient.containerStatistics(id)
            else -> containerStatsCache.get(id).orElse(null)
        }
    }

    fun containerStats(): List<ContainerMetrics> {
        return when {
            status != Status.Available -> emptyList()
            else -> containers().mapNotNull {
                statsForContainer(it.id)
            }
        }
    }

    fun performCommandWithContainer(command: Command): CommandResult {
        if (status == Status.Available) {
            return dockerClient.performCommandWithContainer(command)
        }
        return CommandResult.Unavailable
    }

    fun readLogsForContainer(containerId: String, from: Instant?, to: Instant?): ReadLogsCommandResult {
        if (status == Status.Available) {
            return try {
                ReadLogsCommandResult.Success(dockerClient.readLogsForContainer(containerId, from, to))
            } catch (interruptedException: InterruptedException) {
                ReadLogsCommandResult.TimedOut(DockerClient.READ_LOGS_COMMAND_TIMEOUT_SEC)
            } catch (e: RuntimeException) {
                ReadLogsCommandResult.Failed(e)
            }
        }
        return ReadLogsCommandResult.Unavailable
    }

    fun openContainerLogsConnection(
        containerId: String,
        after: String?,
        before: String?,
        first: Int?,
        last: Int?
    ): DockerLogMessageConnection {

        val beforeTimestamp = before?.decodeAsInstantCursor()
        val afterTimestamp = after?.decodeAsInstantCursor()

        // Filter logs based on the cursor.
        val filteredLogs = dockerClient.readLogLinesForContainer(
            containerId = containerId,
            from = afterTimestamp,
            to = beforeTimestamp
        )

        // Apply pagination (first or last).
        val paginatedLogs = if (first != null) {
            filteredLogs.take(first)
        } else if (last != null) {
            filteredLogs.takeLast(last)
        } else {
            filteredLogs.take(10) // Default page size.
        }

        val edges = paginatedLogs.map {
            DockerLogMessageEdge(
                cursor = it.timestamp.encodeAsCursor(),
                node = it
            )
        }

        return DockerLogMessageConnection(
            edges = edges,
            pageInfo = PageInfo(
                hasNextPage = filteredLogs.size > paginatedLogs.size,
                hasPreviousPage = before != null || after != null,
                startCursor = edges.firstOrNull()?.cursor,
                endCursor = edges.lastOrNull()?.cursor
            )
        )
    }


    private fun checkAvailability(): Status {
        return when {
            !dockerConfiguration.enabled -> {
                Status.Disabled
            }

            else -> {
                when (val result = dockerClient.checkAvailability()) {
                    is DockerClient.PingResult.Fail -> {
                        logger.error("Docker is unavailable because of an error ${result.throwable.message}")
                        Status.Unavailable(result.throwable)
                    }

                    DockerClient.PingResult.Success -> Status.Available
                }
            }
        }
    }
}