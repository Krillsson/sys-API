package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.TimeUnit

data class MonitorConfiguration(
        @JsonProperty val interval: Long,
        @JsonProperty val unit: TimeUnit
)