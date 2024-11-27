package com.krillsson.sysapi.windows.eventlog

import com.krillsson.sysapi.config.WindowsEventLogConfiguration
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator
import java.time.Instant


// https://learn.microsoft.com/en-us/windows/win32/eventlog/event-sources
// https://learn.microsoft.com/en-us/previous-versions/aa363673(v=vs.85)
class WindowsEventLogReader(private val config: WindowsEventLogConfiguration) {
    companion object {
        private const val EVENT_LOG_SOURCE_APPLICATION = "Application"
        private const val EVENT_LOG_SOURCE_SECURITY = "Security"
        private const val EVENT_LOG_SOURCE_SYSTEM = "System"
    }

    fun readAllApplication(): List<WindowsEventLogRecord> {
        val iter = EventLogIterator(EVENT_LOG_SOURCE_APPLICATION)
        return readRecords(iter)
    }

    fun readAllSystem(): List<WindowsEventLogRecord> {
        val iter = EventLogIterator(EVENT_LOG_SOURCE_SYSTEM)
        return readRecords(iter)
    }

    fun readAllSecurity(): List<WindowsEventLogRecord> {
        val iter = EventLogIterator(EVENT_LOG_SOURCE_SECURITY)
        return readRecords(iter)
    }

    fun readAllBySource(source: String): List<WindowsEventLogRecord> {
        return readAllApplication().filter { it.source == source }
    }

    private fun readRecords(iter: EventLogIterator): List<WindowsEventLogRecord> {
        return if (config.enabled) {
            val lines = mutableListOf<WindowsEventLogRecord>()
            while (iter.hasNext()) {
                val record = iter.next()
                val timestamp = Instant.ofEpochSecond(record.record.TimeGenerated.toLong())
                val type = record.type.asType()
                val source = record.source
                val category = record.record.EventCategory.toString()
                val message = StringBuilder()
                val strings = record.strings
                if (strings != null) {
                    for (element in strings) {
                        message.append(element.trim())
                    }
                }
                lines += WindowsEventLogRecord(
                    timestamp = timestamp,
                    message = message.toString(),
                    eventType = type,
                    source = source,
                    category = category
                )
            }
            lines
        } else {
            emptyList()
        }
    }

    fun allSources(): List<WindowsEventLogSourceInfo> {
        return if (config.enabled) {
            val iter = EventLogIterator(EVENT_LOG_SOURCE_APPLICATION)
            val lines = mutableListOf<Pair<String, Int>>()

            while (iter.hasNext()) {
                val record = iter.next()
                lines += record.source to record.recordNumber
            }
            lines.groupBy { it.first }
                .map { WindowsEventLogSourceInfo(it.key, it.value.size) }
                .sortedBy { it.recordCount }
        } else {
            emptyList()
        }
    }

    private fun Advapi32Util.EventLogType.asType(): WindowsEventLogRecord.Type {
        return when (this) {
            Advapi32Util.EventLogType.Error -> WindowsEventLogRecord.Type.Error
            Advapi32Util.EventLogType.Warning -> WindowsEventLogRecord.Type.Warning
            Advapi32Util.EventLogType.Informational -> WindowsEventLogRecord.Type.Informational
            Advapi32Util.EventLogType.AuditSuccess -> WindowsEventLogRecord.Type.AuditSuccess
            Advapi32Util.EventLogType.AuditFailure -> WindowsEventLogRecord.Type.AuditFailure
        }
    }
}
