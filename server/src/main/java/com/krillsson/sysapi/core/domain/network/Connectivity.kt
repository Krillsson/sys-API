package com.krillsson.sysapi.core.domain.network

data class Connectivity(
    val externalIp: String?,
    val previousExternalIp: String?,
    val localIp: String?,
    val connected: Boolean
)