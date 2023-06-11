package com.krillsson.sysapi.logaccess.systemd

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.util.ExecuteCommand
import com.krillsson.sysapi.util.logger
import java.time.Clock
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class SystemDaemonJournalReader(
    private val serviceUnitName: String,
    private val mapper: ObjectMapper
) {

    companion object {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    val logger by logger()

    fun name() = serviceUnitName

    fun lines(): List<String> {
        return queryJournalCtl()
    }

    class JournalCtlOutput : ArrayList<JournalCtlOutput.Line>() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Line(
            @JsonProperty("MESSAGE")
            val message: String,
            @JsonProperty("__REALTIME_TIMESTAMP")
            val timestamp: Long
        )
    }

    private fun queryJournalCtl(): List<String> {
        val messages = mutableListOf<String>()
        ExecuteCommand.asBufferedReader("journalctl --output=json UNIT=$serviceUnitName")?.use { reader ->
            val iterator: MappingIterator<JournalCtlOutput.Line> =
                mapper.readerFor(JournalCtlOutput.Line::class.java).readValues(reader)
            val lines = iterator.readAll()
            lines.forEach {
                val instant = Instant.ofEpochSecond(TimeUnit.MICROSECONDS.toSeconds(it.timestamp))
                    .atZone(Clock.systemDefaultZone().zone).toLocalDateTime()
                messages += "[${formatter.format(instant)}] ${it.message}"
            }
        }
        return messages
    }


    fun sizeBytes(): Long {
        return 0
    }
}