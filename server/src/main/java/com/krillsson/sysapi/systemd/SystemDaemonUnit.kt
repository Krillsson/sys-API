package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.util.ExecuteCommand
import com.krillsson.sysapi.util.logger
import java.time.Instant
import java.util.concurrent.TimeUnit


class SystemDaemonUnit(
    private val serviceUnitName: String,
    private val mapper: ObjectMapper
) {

    companion object {
        private const val GET_LOG_ENTRIES_COMMAND = "journalctl --output=json UNIT=%s"
    }

    val logger by logger()

    fun name() = serviceUnitName

    fun lines(): List<SystemDaemonJournalEntry> {
        val messages = mutableListOf<SystemDaemonJournalEntry>()
        ExecuteCommand.asBufferedReader(String.format(GET_LOG_ENTRIES_COMMAND, serviceUnitName))?.use { reader ->
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

    fun performCommand(command: SystemDaemonCommand): CommandResult {
        val exitStatus = ExecuteCommand.asExitStatus("systemctl ${command.name.lowercase()} $serviceUnitName")
        return exitStatus.fold(
            onSuccess = {
                if (it == 0) {
                    CommandResult.Success
                } else {
                    CommandResult.Failed(RuntimeException("Failed with $it"))
                }
            },
            onFailure = {
                CommandResult.Failed(it)
            }
        )
    }
}