package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class UpnpIgdConfiguration(
    @JsonProperty val enabled: Boolean,
)