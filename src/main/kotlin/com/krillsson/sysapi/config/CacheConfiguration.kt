package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.TimeUnit

data class CacheConfiguration(
        val enabled: Boolean = true,
        val duration: Long = 5,
        val unit: TimeUnit = TimeUnit.SECONDS
)