package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.TimeUnit

data class CacheConfiguration(
    var enabled: Boolean = true,
    var duration: Long = 5,
    var unit: TimeUnit = TimeUnit.SECONDS
)