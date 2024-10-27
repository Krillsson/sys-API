package com.krillsson.sysapi.docker

import com.github.dockerjava.api.model.Frame
import java.time.Instant

data class DockerLogLine(
    val type: StreamType,
    val level: Level,
    val date: Instant,
    val message: String
)

enum class Level {
    INFO,
    WARN,
    ERROR
}

enum class StreamType {
    STDIN, STDOUT, STDERR, RAW, EMPTY
}

fun Frame.asLogLine(): DockerLogLine {
    val message = String(payload).trim()
    val streamType = try {
        StreamType.valueOf(streamType.name)
    } catch (exception: Exception){
        StreamType.EMPTY
    }
    val dateAndRemainder = message.attemptExtractDate()
    val level = when {
        streamType == StreamType.STDERR -> Level.ERROR
        message.contains("warn", ignoreCase = true) -> Level.WARN
        message.contains("err", ignoreCase = true) -> Level.ERROR
        else -> Level.INFO
    }
    return DockerLogLine(
        streamType,
        level,
        dateAndRemainder,
        message
    )
}

private fun String.attemptExtractDate(): Instant {
    return try {
        Instant.parse(substringBefore(" "))
    } catch (exception: Exception) {
        Instant.ofEpochSecond(0)
    }
}