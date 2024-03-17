package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class HistoryPurgingConfiguration(
    @JsonProperty var olderThan: Long,
    @JsonProperty var unit: ChronoUnit,
    @JsonProperty var purgeEvery: Long,
    @JsonProperty var purgeEveryUnit: TimeUnit
)