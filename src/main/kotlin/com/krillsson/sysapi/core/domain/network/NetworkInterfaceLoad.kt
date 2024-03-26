package com.krillsson.sysapi.core.domain.network

class NetworkInterfaceLoad(
    val name: String,
    val mac: String,
    val isUp: Boolean,
    val values: NetworkInterfaceValues,
    val speed: NetworkInterfaceSpeed
) 