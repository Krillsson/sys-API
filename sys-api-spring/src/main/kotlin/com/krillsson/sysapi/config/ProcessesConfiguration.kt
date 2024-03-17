package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class ProcessesConfiguration(
    @JsonProperty var enabled: Boolean = true
)