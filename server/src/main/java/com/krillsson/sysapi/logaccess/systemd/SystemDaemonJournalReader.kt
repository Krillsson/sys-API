package com.krillsson.sysapi.logaccess.systemd

import org.metabit.platform.interfacing.jjournal.Journal
import org.metabit.platform.interfacing.jjournal.JournalField
import org.metabit.platform.interfacing.jjournal.OpenFlags
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*


class SystemDaemonJournalReader(private val serviceUnitName: String) {

    fun name() = serviceUnitName

    fun lines(): List<String> {
        val messages = mutableListOf<String>()
        return doWithFilteredJournal { journal ->
            val threeDaysAgo = OffsetDateTime.now().minusDays(3).toInstant()
            journal.moveToEarliest()
            journal.moveForwardUntilTime(threeDaysAgo)
            journal.foreachInTimerange(threeDaysAgo, Instant.now(), Int.MAX_VALUE) {
                messages.add(
                    "${it.timestampAsInstant} ${it.readMessageField()}"
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