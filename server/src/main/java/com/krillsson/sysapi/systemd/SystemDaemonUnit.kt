package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.util.ExecuteCommand
import com.krillsson.sysapi.util.logger
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class SystemDaemonUnit(
    private val serviceUnitName: String,
    private val mapper: ObjectMapper
) {

    companion object {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    val logger by logger()

    fun name() = serviceUnitName

    fun lines(): List<SystemDaemonJournalEntry> {
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

    data class SystemDaemonJournalEntry(
        val timestamp: Instant,
        val message: String
    )


    private fun queryJournalCtl(): List<SystemDaemonJournalEntry> {
        val messages = mutableListOf<SystemDaemonJournalEntry>()
        ExecuteCommand.asBufferedReader("journalctl --output=json UNIT=$serviceUnitName")?.use { reader ->
            val iterator: MappingIterator<JournalCtlOutput.Line> =
                mapper.readerFor(JournalCtlOutput.Line::class.java).readValues(reader)
            val lines = iterator.readAll()
            lines.forEach {
                val instant = Instant.ofEpochSecond(TimeUnit.MICROSECONDS.toSeconds(it.timestamp))
                messages += SystemDaemonJournalEntry(instant, it.message)
            }
        }
        return messages
    }

    fun performCommand(command: SystemDaemonCommand): SystemDaemonJournalManager.CommandResult {
        val exitStatus = ExecuteCommand.asExitStatus("systemctl ${command.name.lowercase()} $serviceUnitName")
        return exitStatus.fold(
            onSuccess = {
                if (it == 0) {
                    SystemDaemonJournalManager.CommandResult.Success
                } else {
                    SystemDaemonJournalManager.CommandResult.Failed(RuntimeException("Failed with $it"))
                }
            },
            onFailure = {
                SystemDaemonJournalManager.CommandResult.Failed(it)
            }
        )
    }
}