package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

class JournalCtlOutput : ArrayList<JournalCtlOutput.Line>() {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Line(
        @JsonProperty("MESSAGE")
        val message: String,
        @JsonProperty("__REALTIME_TIMESTAMP")
        val timestamp: Long
    )
}