package com.krillsson.sysapi.core.domain.docker

data class Network(
    val aliases: List<String>,
    val endpointID: String,
    val gateway: String,
    val globalIPv6Address: String,
    val globalIPv6PrefixLen: Int,
    val iPAMConfig: Ipam,
    val iPAddress: String,
    val iPPrefixLen: Int,
    val iPv6Gateway: String,
    val links: List<Link>,
    val macAddress: String,
    val networkID: String
)