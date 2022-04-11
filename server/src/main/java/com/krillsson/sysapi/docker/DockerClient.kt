package com.krillsson.sysapi.docker

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientConfigDelegate
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.config.DockerConfiguration
import com.krillsson.sysapi.core.domain.docker.Command
import com.krillsson.sysapi.core.domain.docker.CommandType
import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import java.time.OffsetDateTime
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

    private val config: DockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerTlsVerify(false)
        .build()
        .apply {
            object : DockerClientConfigDelegate(this) {
                override fun getObjectMapper(): ObjectMapper {
                    return applicationObjectMapper
                }
            }
        }

    private val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
        .dockerHost(config.dockerHost)
        .sslConfig(config.sslConfig)
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

    fun readLogsForContainer(containerId: String, from: OffsetDateTime?, to: OffsetDateTime?): ReadLogsCommandResult {
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

    private fun readLogsForContainerInternal(
        containerId: String,
        from: OffsetDateTime?,
        to: OffsetDateTime?
    ): List<String> {
        return if (status == Status.Available) {
            val timedResult = measureTimeMillis {
                val result = mutableListOf<String>()
                client.logContainerCmd(containerId)
                    .withFollowStream(false)
                    .withStdErr(true)
                    .withStdOut(true)
                    .withTimestamps(true)
                    .apply { from?.let { withSince(from.toEpochSecond().toInt()) } }
                    .apply { to?.let { withUntil(to.toEpochSecond().toInt()) } }
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
            !dockerConfiguration.enabled() -> {
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






