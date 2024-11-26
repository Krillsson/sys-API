package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.JournalLogsConfiguration
import com.krillsson.sysapi.util.logger
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class JournalCtl(
    private val mapper: ObjectMapper,
    private val journalLogsConfiguration: JournalLogsConfiguration
) {
    companion object {
        private const val GET_LOG_ENTRIES_COMMAND = "journalctl --output=json UNIT=%s"

        private const val GET_LOG_ENTRIES_FILTER_SINCE = "--since \"%s\""
        private const val GET_LOG_ENTRIES_FILTER_UNTIL = "--until \"%s\""
        private const val GET_LOG_ENTRIES_LIMIT = "-n %d"
    }

    val logger by logger()

    class JournalForServiceOutput : ArrayList<JournalForServiceOutput.Line>() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Line(
            @JsonProperty("MESSAGE")
            val message: String,
            @JsonProperty("__REALTIME_TIMESTAMP")
            val timestamp: Long
        )
    }

    fun supportedBySystem(): Boolean {
        return Bash.checkIfCommandExists("journalctl").getOrNull() ?: false
    }

    fun lines(
        serviceUnitName: String,
        limit: Int? = null,
        since: Instant? = null,
        until: Instant? = null
    ): List<SystemDaemonJournalEntry> {
        return if (journalLogsConfiguration.enabled) {
            val messages = mutableListOf<SystemDaemonJournalEntry>()
            val command = when {
                limit != null -> {
                    buildString {
                        append(GET_LOG_ENTRIES_COMMAND.format(serviceUnitName))
                        append(" ")
                        append(GET_LOG_ENTRIES_LIMIT.format(limit))
                    }
                }

                else -> {
                    buildString {
                        append(GET_LOG_ENTRIES_COMMAND.format(serviceUnitName))
                        if (since != null) {
                            append(" ")
                            append(GET_LOG_ENTRIES_FILTER_SINCE.format(DateTimeFormatter.ISO_DATE_TIME.format(since)))
                        }
                        if (until != null) {
                            append(" ")
                            append(GET_LOG_ENTRIES_FILTER_UNTIL.format(DateTimeFormatter.ISO_DATE_TIME.format(until)))
                        }
                    }
                }
            }
            val result = Bash.executeToText(String.format(GET_LOG_ENTRIES_COMMAND, serviceUnitName))
            result.map { json ->
                val iterator: MappingIterator<JournalForServiceOutput.Line> =
                    mapper.readerFor(JournalForServiceOutput.Line::class.java).readValues(json)
                val lines = iterator.readAll()
                lines.forEach {
                    val instant = Instant.ofEpochSecond(TimeUnit.MICROSECONDS.toSeconds(it.timestamp))
                    messages += SystemDaemonJournalEntry(instant, it.message)
                }
            }
            messages
        } else {
            emptyList()
        }
    }
}