package com.krillsson.sysapi.core.history

import com.google.common.annotations.VisibleForTesting
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.HistorySystemLoadDAO
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.util.Clock
import com.krillsson.sysapi.util.logger
import io.dropwizard.hibernate.UnitOfWork
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

open class HistoryRepository @VisibleForTesting constructor(
    private val clock: Clock,
    private val dao: HistorySystemLoadDAO
) {

    val logger by logger()

    constructor(store: HistorySystemLoadDAO) : this(Clock(), store) {}

    @UnitOfWork
    open fun get(): List<SystemHistoryEntry> {
        return dao.findAll().map { it.asSystemHistoryEntry() }
    }

    @UnitOfWork
    open fun record(load: HistorySystemLoad) {
        val entry = SystemHistoryEntry(clock.now(), load)
        logger.trace("Recording history for {}", entry)
        dao.insert(entry.asEntity())
    }

    @UnitOfWork
    open fun purge(olderThan: Int, unit: ChronoUnit) {
        val maxAge = clock.now().minus(olderThan.toLong(), unit)
        logger.info("Purging history older than {}", maxAge)
        dao.purge(maxAge)
    }

    @UnitOfWork
    open fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<SystemHistoryEntry> {
        if (fromDate == null && toDate == null) {
            return get()
        }
        val from = fromDate ?: OffsetDateTime.MIN
        val to = toDate ?: OffsetDateTime.MAX
        return dao.findAllBetween(from, to).map { it.asSystemHistoryEntry() }
    }
}