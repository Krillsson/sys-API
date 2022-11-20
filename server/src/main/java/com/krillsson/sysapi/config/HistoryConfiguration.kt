package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.concurrent.TimeUnit

class HistoryConfiguration(
    @JsonProperty val purging: HistoryPurgingConfiguration,
    @JsonProperty val interval: Long,
    @JsonProperty val unit: TimeUnit
)