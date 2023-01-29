package com.krillsson.sysapi.config

import com.fasterxml.jackson.annotation.JsonProperty

data class MdnsConfiguration(
    @JsonProperty val enabled: Boolean,
)