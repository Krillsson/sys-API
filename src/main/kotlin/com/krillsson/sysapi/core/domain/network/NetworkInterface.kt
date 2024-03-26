package com.krillsson.sysapi.core.domain.network

class NetworkInterface(
    val name: String,
    val displayName: String,
    val mac: String,
    val speedBitsPerSeconds: Long,
    val mtu: Long,
    val isLoopback: Boolean,
    val ipv4: List<String>,
    val ipv6: List<String>
) 