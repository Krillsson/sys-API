package com.krillsson.sysapi.core.domain.docker

data class VolumeBinding(
    val hostPath: String,
    val containerPath: String
)