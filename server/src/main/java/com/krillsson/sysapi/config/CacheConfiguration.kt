package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.TimeUnit

data class CacheConfiguration(
    @JsonProperty val duration: Long,
    @JsonProperty val unit: TimeUnit
)