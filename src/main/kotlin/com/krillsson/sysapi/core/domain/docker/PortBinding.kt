package com.krillsson.sysapi.core.domain.docker

data class PortBinding(
    val ip: String,
    val privatePort: Int,
    val publicPort: Int,
    val type: String
)