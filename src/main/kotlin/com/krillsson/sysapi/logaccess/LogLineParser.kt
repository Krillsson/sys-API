package com.krillsson.sysapi.logaccess

import org.springframework.stereotype.Component
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Component
class LogLineParser {
    fun parseLine(line: String): LogMessage {
        val timestamp = detectAndParseTimestamp(line)
        val logLevel = detectLogLevel(line)

        val messageStartIndex = listOfNotNull(timestamp?.second?.last, logLevel?.second?.last).maxOrNull()?.let { highestEndIndex -> highestEndIndex + 1 }

        val message = messageStartIndex?.let { line.substring(messageStartIndex).trim() } ?: line
        return LogMessage(
            message,
            logLevel?.first ?: LogMessage.Level.UNKNOWN,
            timestamp?.first
        )
    }


    fun detectAndParseTimestamp(logLine: String): Pair<Instant, IntRange>? {
        val matchResult = timestampRegex.find(logLine) ?: return null
        var result: Pair<Instant, IntRange>? = null
        val parsers = listOf<(MatchResult) -> Pair<Instant, IntRange>?>(
            { it.attemptParseAsZonedDateTime() },
            { it.attemptParseAsOffsetDateTime() },
            { it.attemptParseAsLocalDateTime() }
        )

        for (parser in parsers) {
            result = parser(matchResult)
            if (result != null) {
                break
            }
        }

        return result
    }

    private fun MatchResult.attemptParseAsOffsetDateTime(): Pair<Instant, IntRange>? {
        return try {
            OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant() to range
        } catch (e: DateTimeParseException) {
            // Ignore and try the next format
            null
        }
    }

    private fun MatchResult.attemptParseAsZonedDateTime(): Pair<Instant, IntRange>? {
        return try {
            ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant() to range
        } catch (e: DateTimeParseException) {
            // Ignore and try the next format
            null
        }
    }

    private fun MatchResult.attemptParseAsLocalDateTime(): Pair<Instant, IntRange>? {
        var result: Pair<Instant, IntRange>? = null
        for (format in timestampFormats) {
            result = try {
                LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format)).atZone(ZoneId.systemDefault()).toInstant() to range
            } catch (e: DateTimeParseException) {
                // Ignore and try the next format
                null
            }
            if (result != null) {
                break
            }
        }

        return result
    }


    fun detectLogLevel(logLine: String): Pair<LogMessage.Level, IntRange>? {
        val matchResult = logLevelRegex.find(logLine)
        return matchResult?.value?.uppercase()?.let {
            when {
                it.contains("err", ignoreCase = true) -> LogMessage.Level.ERROR to matchResult.range
                it.contains("warn", ignoreCase = true) -> LogMessage.Level.WARN to matchResult.range
                else -> LogMessage.Level.valueOf(it) to matchResult.range
            }

        }
    }

    companion object {
        private val timestampFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss",           // ISO 8601
            "yyyy-MM-dd HH:mm:ss",            // Standard format
            "yyyy-MM-dd HH:mm:ss.SSS",        // Standard format with milliseconds
            "MM/dd/yyyy HH:mm:ss",            // US format
            "dd/MM/yyyy HH:mm:ss",            // European format
            "yyyy-MM-dd'T'HH:mm:ss.SSS",      // ISO with milliseconds
            "yyyy/MM/dd HH:mm:ss",            // Slash-separated
            "dd-MM-yyyy HH:mm:ss",             // Day-Month-Year format
        )

        private val timestampRegex = Regex(
            """\b(\d{4}[-/]\d{2}[-/]\d{2}[T ]\d{2}:\d{2}:\d{2}(?:\.\d{3}(?:[+-]\d{2}:\d{2})?)?)\b"""
        )

        private val logLevelRegex = Regex("""\b(TRACE|DEBUG|INFO|WARN|WARNING|ERROR|ERR|FATAL)\b""", RegexOption.IGNORE_CASE)
    }
}