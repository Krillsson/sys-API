package com.krillsson.sysapi.logaccess.systemd

import org.metabit.platform.interfacing.jjournal.Journal
import org.metabit.platform.interfacing.jjournal.JournalField
import org.metabit.platform.interfacing.jjournal.OpenFlags
import java.time.format.DateTimeFormatter
import java.util.*


class SystemDaemonJournalReader(private val serviceUnitName: String) {

    companion object {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    fun name() = serviceUnitName

    fun lines(): List<String> {
        val messages = mutableListOf<String>()
        return doWithFilteredJournal { journal ->
            journal.moveToEarliest()
            journal.foreachInTimerange(
                journal.firstAndLastInstant.from,
                journal.firstAndLastInstant.until,
                Int.MAX_VALUE
            ) {
                messages.add(
                    "[${formatter.format(it.timestampAsInstant)}] ${it.readMessageField()}"
                )
            }
            messages
        }
    }

    fun sizeBytes(): Long {
        return doWithFilteredJournal {
            it.bytesUsedByJournalFiles
        }
    }

    private fun <T> doWithFilteredJournal(function: (Journal) -> T): T {
        return Journal(EnumSet.of(OpenFlags.CURRENT_USER)).use { journal ->
            journal.filteringReset();
            journal.filteringAddFilterExpressionExactMatch(JournalField.SYSTEMD_USER_UNIT.value, serviceUnitName)
            journal.moveToEarliest()
            function(journal)
        }
    }
}