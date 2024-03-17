package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class HistoryConfiguration(
    @JsonProperty var purging: HistoryPurgingConfiguration = HistoryPurgingConfiguration(14, ChronoUnit.DAYS, 1, TimeUnit.DAYS),
    @JsonProperty var intervar: Long = 30,
    @JsonProperty var unit: TimeUnit = TimeUnit.MINUTES
)