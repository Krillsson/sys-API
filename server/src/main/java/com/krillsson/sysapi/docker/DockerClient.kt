package com.krillsson.sysapi.docker

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.dockerjava.core.DockerClientConfig
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

class DockerClient(
    cacheConfiguration: CacheConfiguration,
    private val dockerConfiguration: DockerConfiguration,
    private val objectMapper: ObjectMapper
) {

    companion object {
        val LOGGER by logger()
    }

    private val config: DockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder(objectMapper)
        .withDockerTlsVerify(false)
        .build()

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

    val status = checkAvailability()

    fun listContainers() = containersCache.get()

    sealed class CommandResult {
        object Success : CommandResult()
        object Unavailable : CommandResult()
        data class Failed(val error: RuntimeException) : CommandResult()
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

    private fun checkAvailability(): Status {
        return when {
            !dockerConfiguration.enabled() -> {
                Status.Disabled
            }
            else -> try {
                client.pingCmd().exec()
                Status.Available
            } catch (err: RuntimeException) {
                LOGGER.error("Unable to access Docker instance", err)
                Status.Unavailable(err)
            }
        }
    }

}






