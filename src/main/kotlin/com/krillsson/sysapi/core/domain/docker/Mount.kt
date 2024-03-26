package com.krillsson.sysapi.core.domain.docker

data class Mount(
    val destination: String,
    val driver: String,
    val mode: String,
    val name: String,
    val propagation: String,
    val rw: Boolean,
    val source: String
)