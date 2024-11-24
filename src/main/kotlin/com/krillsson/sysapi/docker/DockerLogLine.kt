package com.krillsson.sysapi.docker

import com.krillsson.sysapi.logaccess.LogMessage
import java.time.Instant

data class DockerLogLine(
    val streamType: StreamType,
    val level: LogMessage.Level,
    val timestamp: Instant,
    val message: String
)

enum class StreamType {
    STDIN, STDOUT, STDERR, RAW, EMPTY
}