package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.TimeUnit

data class MonitorConfiguration(
    @JsonProperty var intervar: Long,
    @JsonProperty var unit: TimeUnit
)