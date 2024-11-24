package com.krillsson.sysapi.docker

import com.github.dockerjava.api.model.Frame
import com.krillsson.sysapi.logaccess.LogLineParser
import com.krillsson.sysapi.logaccess.LogMessage
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DockerLogLineParser(private val logLineParser: LogLineParser) {
    fun parseFrame(frame: Frame): DockerLogMessage {
        val line = String(frame.payload).trim()
        val timestamp = logLineParser.detectAndParseTimestamp(line)
        val logLevel = logLineParser.detectLogLevel(line)

        val streamType = try {
            StreamType.valueOf(frame.streamType.name)
        } catch (exception: Exception) {
            StreamType.EMPTY
        }

        val messageStartIndex = listOfNotNull(timestamp?.second?.last, logLevel?.second?.last).maxOrNull()?.let { highestEndIndex -> highestEndIndex + 1 }

        val message = messageStartIndex?.let { line.substring(messageStartIndex).trim() } ?: line
        return DockerLogMessage(
            streamType = streamType,
            message = message,
            level = logLevel?.first ?: LogMessage.Level.UNKNOWN,
            timestamp = timestamp?.first ?: Instant.EPOCH
        )
    }
}