package com.krillsson.sysapi.graphql.domain

interface Docker
object DockerAvailable : Docker

data class DockerUnavailable(
        val reason: String,
        val isDisabled: Boolean
) : Docker

interface ReadLogsForContainerOutput

data class ReadLogsForContainerOutputSucceeded(
        val lines: List<String>
) : ReadLogsForContainerOutput

data class ReadLogsForContainerOutputFailed(
        val reason: String
) : ReadLogsForContainerOutput
