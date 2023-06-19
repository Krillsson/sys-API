package com.krillsson.sysapi.logaccess.windowseventlog

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator
import java.time.Instant


class WindowsEventLogReader(val name: String) {

    fun readAll(): List<WindowsEventLogRecord> {
        val lines = mutableListOf<WindowsEventLogRecord>()
        val iter = EventLogIterator(name)

        while (iter.hasNext()) {
            val record = iter.next()
            val timestamp = Instant.ofEpochSecond(record.record.TimeGenerated.toLong())
            val type = record.type.asType()
            val source = record.source
            val category = record.record.EventCategory.toString()
            val message = StringBuilder()
            for (i in 0 until record.strings.size) {
                message.append(record.strings[i].trim())
            }
            lines += WindowsEventLogRecord(
                timestamp = timestamp,
                message = message.toString(),
                eventType = type,
                source = source,
                category = category
            )
        }
        return lines
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
