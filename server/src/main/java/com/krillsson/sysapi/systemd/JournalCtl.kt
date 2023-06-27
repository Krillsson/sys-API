package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.JournalLogsConfiguration
import com.krillsson.sysapi.util.ExecuteCommand
import java.time.Instant
import java.util.concurrent.TimeUnit

class JournalCtl(
    private val mapper: ObjectMapper,
    private val journalLogsConfiguration: JournalLogsConfiguration
) {
    companion object {
        private const val GET_LOG_ENTRIES_COMMAND = "journalctl --output=json UNIT=%s"
    }

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
        return ExecuteCommand.checkIfCommandExistsUsingBash("journalctl").getOrNull() ?: false
    }

    fun lines(serviceUnitName: String): List<SystemDaemonJournalEntry> {
        return if (journalLogsConfiguration.enabled) {
            val messages = mutableListOf<SystemDaemonJournalEntry>()
            ExecuteCommand.asBufferedReader(String.format(GET_LOG_ENTRIES_COMMAND, serviceUnitName))?.use { reader ->
                val iterator: MappingIterator<JournalForServiceOutput.Line> =
                    mapper.readerFor(JournalForServiceOutput.Line::class.java).readValues(reader)
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