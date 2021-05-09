package com.krillsson.sysapi.graphql.domain

interface Docker

data class DockerUnavailable(
    val reason: String,
    val isDisabled: Boolean
) : Docker

object DockerAvailable : Docker
