package com.krillsson.sysapi.core.history

import com.google.common.annotations.VisibleForTesting
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.persistence.Store
import com.krillsson.sysapi.util.Clock
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.function.Consumer
import java.util.stream.Collectors

class HistoryRepository @VisibleForTesting constructor(
    private val clock: Clock,
    private val store: Store<List<SystemHistoryEntry>>
) {

    constructor(store: Store<List<SystemHistoryEntry>>) : this(Clock(), store) {}

    fun get(): List<SystemHistoryEntry> {
        return store.read()?.toList().orEmpty()
    }

    fun record(load: HistorySystemLoad) {
        val entry = SystemHistoryEntry(clock.now(), load)
        LOGGER.trace("Recording history for {}", entry)
        store.update { entries ->
            val mutableEntries = entries?.toMutableList() ?: mutableListOf()
            mutableEntries.add(entry)
            mutableEntries.toList()
        }
    }

    fun purge(olderThan: Int, unit: ChronoUnit) {
        val maxAge = clock.now().minus(olderThan.toLong(), unit)
        store.update { entries ->
            val mutableEntries = entries?.toMutableList() ?: mutableListOf()
            val toBeRemoved: MutableSet<SystemHistoryEntry> =
                HashSet()
            for (historyEntry in mutableEntries) {
                if (historyEntry.date.isBefore(maxAge)) {
                    toBeRemoved.add(historyEntry)
                }
            }
            LOGGER.debug(
                "Purging {} entries older than {} {}",
                toBeRemoved.size,
                olderThan,
                unit.name
            )
            toBeRemoved.forEach(Consumer { entry: SystemHistoryEntry? ->
                mutableEntries.remove(
                    entry
                )
            })
            mutableEntries.toList()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HistoryRepository::class.java)
    }

    fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<SystemHistoryEntry> {
        if (fromDate == null && toDate == null) {
            return get()
        }
        val from = fromDate ?: OffsetDateTime.MIN
        val to = toDate ?: OffsetDateTime.MAX
        return store.read().orEmpty()
            .stream()
            .filter { e: SystemHistoryEntry ->
                e.date.isAfter(from) && e.date.isBefore(to)
            }
            .collect(Collectors.toList())
    }
}