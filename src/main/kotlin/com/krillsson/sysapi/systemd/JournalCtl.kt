package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.JournalLogsConfiguration
import com.krillsson.sysapi.util.logger
import reactor.core.publisher.Flux
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

        private const val derpderp = "journalctl -u serviceName -f"
        private const val GET_LOG_LINES_FOLLOW = "--lines=all --follow"
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

    fun tailLines(
        serviceUnitName: String,
        since: Instant? = null
    ): Flux<SystemDaemonJournalEntry> {
        return if (journalLogsConfiguration.enabled) {
            val command =
                buildString {
                    append(GET_LOG_ENTRIES_COMMAND.format(serviceUnitName))
                    if (since != null) {
                        append(" ")
                        append(GET_LOG_ENTRIES_FILTER_SINCE.format(DateTimeFormatter.ISO_DATE_TIME.format(since)))
                    }
                    append(" ")
                    append(GET_LOG_LINES_FOLLOW)
                }
            Bash.executeToTextContinuously(command)
                .map { it.convertJsonStringToEntries() }
                .flatMapIterable { it }
        } else {
            Flux.empty<SystemDaemonJournalEntry>()
        }
    }

    fun lines(
        serviceUnitName: String,
        limit: Int? = null,
        since: Instant? = null,
        until: Instant? = null
    ): List<SystemDaemonJournalEntry> {
        return if (journalLogsConfiguration.enabled) {
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
            val result = Bash.executeToText(command)
            result.getOrNull()?.convertJsonStringToEntries().orEmpty()
        } else {
            emptyList()
        }
    }

    private fun String.convertJsonStringToEntries(): List<SystemDaemonJournalEntry> {
        val messages = mutableListOf<SystemDaemonJournalEntry>()
        val iterator: MappingIterator<JournalForServiceOutput.Line> =
            mapper.readerFor(JournalForServiceOutput.Line::class.java).readValues(this)
        val lines = iterator.readAll()
        lines.forEach {
            val instant = Instant.ofEpochSecond(TimeUnit.MICROSECONDS.toSeconds(it.timestamp))
            messages += SystemDaemonJournalEntry(instant, it.message)
        }
        return messages
    }
}