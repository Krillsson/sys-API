package com.krillsson.sysapi.core.history

import com.google.common.annotations.VisibleForTesting
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.util.Clock
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.HashSet
import java.util.function.Consumer
import java.util.stream.Collectors

class History<T> @VisibleForTesting constructor(private val clock: Clock) {
    private val history: MutableList<HistoryEntry<T>> = mutableListOf()

    constructor() : this(Clock()) {}

    fun get(): List<HistoryEntry<T>> {
        return history.toList()
    }

    fun record(value: T) {
        LOGGER.trace("Recording history for {}", value)
        history.add(HistoryEntry(clock.now(), value))
    }

    fun purge(olderThan: Int, unit: ChronoUnit) {
        val maxAge = clock.now().minus(olderThan.toLong(), unit)
        val toBeRemoved: MutableSet<HistoryEntry<*>> =
            HashSet()
        for (historyEntry in history) {
            if (historyEntry.date.isBefore(maxAge)) {
                toBeRemoved.add(historyEntry)
            }
        }
        LOGGER.trace("Purging {} entries older than {} {}", toBeRemoved.size, olderThan, unit.name)
        toBeRemoved.forEach(Consumer { entry: HistoryEntry<*>? ->
            history.remove(
                entry
            )
        })
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(History::class.java)
    }

    fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<T>> {
        if (fromDate == null && toDate == null) {
            return get()
        }
        val from = fromDate ?: OffsetDateTime.MIN
        val to = toDate ?: OffsetDateTime.MAX
        return history
            .stream()
            .filter { e: HistoryEntry<*> ->
                e.date.isAfter(from) && e.date.isBefore(to)
            }
            .collect(Collectors.toList())
    }
}