package com.krillsson.sysapi.core.domain.docker

data class PortConfig(
    val port: Int,
    val portProtocol: PortProtocol
)