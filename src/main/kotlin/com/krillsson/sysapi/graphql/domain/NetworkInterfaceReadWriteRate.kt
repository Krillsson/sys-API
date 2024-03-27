package com.krillsson.sysapi.graphql.domain

data class NetworkInterfaceReadWriteRate(
    val receiveBytesPerSecond: Long,
    val sendBytesPerSecond: Long
)