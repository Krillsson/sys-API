package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class HistoryPurgingConfiguration(
    @JsonProperty val olderThan: Long,
    @JsonProperty val unit: ChronoUnit,
    @JsonProperty val purgeEvery: Long,
    @JsonProperty val purgeEveryUnit: TimeUnit
)