package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.docker.ContainerMetrics
import com.krillsson.sysapi.core.domain.docker.ContainerMetricsHistoryEntry
import com.krillsson.sysapi.core.history.db.ContainerStatisticsDAO
import com.krillsson.sysapi.util.Clock
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import io.dropwizard.hibernate.UnitOfWork
import java.time.Instant
import java.time.temporal.ChronoUnit

open class ContainersHistoryRepository(
    private val clock: Clock,
    private val containerStatisticsDAO: ContainerStatisticsDAO
) {

    val logger by logger()

    @UnitOfWork(readOnly = true)
    open fun getHistoryLimitedToDates(
        containerId: String,
        fromDate: Instant,
        toDate: Instant
    ): List<ContainerMetricsHistoryEntry> {
        val result = measureTimeMillis {
            containerStatisticsDAO.findAllBetween(containerId, fromDate, toDate)
        }
        logger.info(
            "Took {} to fetch {} history entries",
            "${result.first.toInt()}ms",
            result.second.size
        )
        return result.second.map { it.asContainerStatisticsHistoryEntry() }
    }

    @UnitOfWork(readOnly = true)
    open fun getLatestHistoryEntry(
        containerId: String
    ): ContainerMetricsHistoryEntry? {
        val result = measureTimeMillis {
            containerStatisticsDAO.findLatest(containerId)
        }
        logger.info(
            "Took {} to fetch history entry",
            "${result.first.toInt()}ms",
        )
        return result.second?.asContainerStatisticsHistoryEntry()
    }

    @UnitOfWork
    open fun recordContainerStatistics(statistics: List<ContainerMetrics>) {
        val entries = statistics.map { it.asEntity() }
        containerStatisticsDAO.insertAll(entries)
    }

    @UnitOfWork
    open fun purgeContainerStatistics(olderThan: Long, unit: ChronoUnit) {
        val maxAge = clock.instant().minus(olderThan, unit)
        val deletedCount = containerStatisticsDAO.purge(maxAge)
        logger.info("Purged $deletedCount history older than {}", maxAge)
    }

}