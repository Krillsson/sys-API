package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

class HistoryConfiguration(
    @JsonProperty val purging: HistoryPurgingConfiguration
)