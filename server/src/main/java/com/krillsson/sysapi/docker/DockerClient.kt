package com.krillsson.sysapi.docker

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.api.model.Statistics
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientConfigDelegate
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.core.InvocationBuilder.AsyncResultCallback
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.config.DockerConfiguration
import com.krillsson.sysapi.core.domain.docker.Command
import com.krillsson.sysapi.core.domain.docker.CommandType
import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.docker.ContainerStatistics
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

class DockerClient(
    cacheConfiguration: CacheConfiguration,
    private val dockerConfiguration: DockerConfiguration,
    private val applicationObjectMapper: ObjectMapper
) {

    companion object {
        val LOGGER by logger()
        const val READ_LOGS_COMMAND_TIMEOUT_SEC = 10L
    }

    private val defaultConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .apply {
            dockerConfiguration.host?.let { host ->
                withDockerHost(host)
            }
        }
        .withDockerTlsVerify(false)
        .build()
    private val config: DockerClientConfig = object : DockerClientConfigDelegate(
        defaultConfig
    ) {
        override fun getObjectMapper(): ObjectMapper {
            return applicationObjectMapper
        }
    }

    private val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
        .dockerHost(config.dockerHost)
        .sslConfig(config.sslConfig)
        .connectionTimeout(Duration.ofSeconds(15))
        .responseTimeout(Duration.ofSeconds(30))
        .maxConnections(100)
        .build()

    private val client = DockerClientImpl.getInstance(config, httpClient)

    private val containersCache: Supplier<List<Container>> = Suppliers.memoizeWithExpiration(
        { listContainersInternal() },
        cacheConfiguration.duration, cacheConfiguration.unit
    )

    sealed class Status {
        object Available : Status()
        object Disabled : Status()
        data class Unavailable(val error: RuntimeException) : Status()
    }

    sealed class CommandResult {
        object Success : CommandResult()
        object Unavailable : CommandResult()
        data class Failed(val error: RuntimeException) : CommandResult()
    }

    sealed class ReadLogsCommandResult {
        data class Success(val lines: List<String>) : ReadLogsCommandResult()
        object Unavailable : ReadLogsCommandResult()
        data class Failed(val error: RuntimeException) : ReadLogsCommandResult()
        data class TimedOut(val timeoutSeconds: Long) : ReadLogsCommandResult()
    }

    val status = checkAvailability()

    fun listContainers() = containersCache.get()

    fun container(id: String): Container? {
        return containersCache.get().firstOrNull { it.id == id }
    }

    fun statsForContainer(id: String): ContainerStatistics? {
        return containerStatsInternal(id)
    }

    fun performCommandWithContainer(command: Command): CommandResult {
        if (status == Status.Available) {
            return try {
                val timedResult: Pair<Long, Void> = measureTimeMillis {
                    when (command.commandType) {
                        CommandType.START -> client.startContainerCmd(command.id).exec()
                        CommandType.STOP -> client.stopContainerCmd(command.id).exec()
                        CommandType.PAUSE -> client.pauseContainerCmd(command.id).exec()
                        CommandType.UNPAUSE -> client.unpauseContainerCmd(command.id).exec()
                        CommandType.RESTART -> client.restartContainerCmd(command.id).exec()
                    }
                }
                LOGGER.debug(
                    "Took {} to perform {} with container {}",
                    "${timedResult.first.toInt()}ms",
                    command.commandType,
                    command.id
                )
                CommandResult.Success
            } catch (e: RuntimeException) {
                CommandResult.Failed(e)
            }
        }
        return CommandResult.Unavailable
    }

    fun readLogsForContainer(containerId: String, from: Instant?, to: Instant?): ReadLogsCommandResult {
        if (status == Status.Available) {
            return try {
                ReadLogsCommandResult.Success(readLogsForContainerInternal(containerId, from, to))
            } catch (interruptedException: InterruptedException) {
                ReadLogsCommandResult.TimedOut(READ_LOGS_COMMAND_TIMEOUT_SEC)
            } catch (e: RuntimeException) {
                ReadLogsCommandResult.Failed(e)
            }
        }
        return ReadLogsCommandResult.Unavailable
    }

    private fun listContainersInternal(): List<Container> {
        return if (status == Status.Available) {
            val timedResult = measureTimeMillis {
                val containers = client.listContainersCmd().withShowAll(true).exec()
                containers.map { container ->
                    val inspection = client.inspectContainerCmd(container.id).exec()
                    val volumes = if (inspection.volumes == null) emptyList() else inspection.volumes.asVolumeBindings()
                    val config = inspection.config.asConfig(volumes)
                    val health = inspection.state.health?.asHealth()
                    container.asContainer(config, health)
                }
            }
            LOGGER.debug(
                "Took {} to fetch {} containers",
                "${timedResult.first.toInt()}ms",
                timedResult.second.size
            )
            timedResult.second
        } else {
            emptyList()
        }
    }

    private fun containerStatsInternal(containerId: String): ContainerStatistics? {
        return if (status == Status.Available) {
            var statistics: Statistics? = null
            val callback = AsyncResultCallback<Statistics>()
            client.statsCmd(containerId)
                .withNoStream(true)
                .exec(callback)
                .awaitCompletion(READ_LOGS_COMMAND_TIMEOUT_SEC, TimeUnit.SECONDS)
            try {
                statistics = callback.awaitResult()
                callback.close()
                statistics.asStatistics()
            } catch (exception: Exception) {
                LOGGER.error("Error while getting stats for $containerId", exception)
                null
            }
        } else {
            null
        }
    }

    private fun readLogsForContainerInternal(
        containerId: String,
        from: Instant?,
        to: Instant?
    ): List<String> {
        return if (status == Status.Available) {
            val timedResult = measureTimeMillis {
                val result = mutableListOf<String>()
                client.logContainerCmd(containerId)
                    .withFollowStream(false)
                    .withStdErr(true)
                    .withStdOut(true)
                    .withTimestamps(true)
                    .apply { from?.let { withSince(from.toEpochMilli().div(1000).toInt()) } }
                    .apply { to?.let { withUntil(to.toEpochMilli().div(1000).toInt()) } }
                    .exec(object : ResultCallback.Adapter<Frame>() {
                        override fun onNext(frame: Frame?) {
                            result.add(frame.toString())
                        }
                    }).awaitCompletion(READ_LOGS_COMMAND_TIMEOUT_SEC, TimeUnit.SECONDS)
                result
            }
            LOGGER.debug(
                "Took {} to fetch {} log lines",
                "${timedResult.first.toInt()}ms",
                timedResult.second.size
            )
            timedResult.second
        } else {
            emptyList()
        }
    }

    private fun checkAvailability(): Status {
        return when {
            !dockerConfiguration.enabled -> {
                Status.Disabled
            }

            else -> try {
                client.pingCmd().exec()
                Status.Available
            } catch (err: RuntimeException) {
                Status.Unavailable(err)
            }
        }
    }

}






