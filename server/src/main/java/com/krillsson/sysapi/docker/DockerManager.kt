package com.krillsson.sysapi.docker

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.config.DockerConfiguration
import com.krillsson.sysapi.core.domain.docker.Command
import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.docker.ContainerMetrics
import com.krillsson.sysapi.core.domain.docker.ContainerMetricsHistoryEntry
import com.krillsson.sysapi.core.history.ContainersHistoryRepository
import java.time.Instant
import java.util.*

class DockerManager(
    private val dockerClient: DockerClient,
    private val cacheConfiguration: CacheConfiguration,
    private val containersHistoryRepository: ContainersHistoryRepository,
    private val dockerConfiguration: DockerConfiguration,
) {
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
        return when {
            status != Status.Available -> null
            !cacheConfiguration.enabled -> dockerClient.getContainer(id)
            else -> containersCache.get().firstOrNull { it.id == id }
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
        return if (status == Status.Available) {
            containerStatsCache.get(id).orElse(null)
        } else {
            null
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

    private fun checkAvailability(): Status {
        return when {
            !dockerConfiguration.enabled -> {
                Status.Disabled
            }

            else -> {
                when (val result = dockerClient.checkAvailability()) {
                    is DockerClient.PingResult.Fail -> Status.Unavailable(result.throwable)
                    DockerClient.PingResult.Success -> Status.Available
                }
            }
        }
    }

    private fun listContainers(): List<Container> {
        return if (status == Status.Available) {
            dockerClient.listContainers()
        } else {
            emptyList()
        }
    }
}