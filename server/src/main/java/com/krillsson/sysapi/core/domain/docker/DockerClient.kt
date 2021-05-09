package com.krillsson.sysapi.core.domain.docker

data class Config(
    val env: List<String>,
    val volumeBindings: List<VolumeBinding>,
    val cmd: List<String>,
    val exposedPorts: List<PortConfig>
)

