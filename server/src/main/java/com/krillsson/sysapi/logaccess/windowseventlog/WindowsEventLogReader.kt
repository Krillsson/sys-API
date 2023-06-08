package com.krillsson.sysapi.logaccess.windowseventlog

import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator
import java.time.Instant
import java.time.format.DateTimeFormatter


class WindowsEventLogReader(val name: String) {

    companion object {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    fun readAll(): List<String> {
        val lines = mutableListOf<String>()
        val iter = EventLogIterator(name)
        while (iter.hasNext()) {
            val record = iter.next()
            val timestamp = Instant.ofEpochSecond(record.record.TimeGenerated.toLong())
            val type = record.type.name
            val source = record.source
            val message = StringBuilder()
            for (i in 0 until record.strings.size) {
                message.append(record.strings[i].trim())
            }
            lines += "[${formatter.format(timestamp)}] $type $source $message"
        }
        return lines
    }
}