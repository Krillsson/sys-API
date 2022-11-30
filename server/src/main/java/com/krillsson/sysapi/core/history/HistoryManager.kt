package com.krillsson.sysapi.core.history

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.util.logger
import com.krillsson.sysapi.util.measureTimeMillis
import io.dropwizard.lifecycle.Managed
import java.time.OffsetDateTime

open class HistoryManager constructor(
    private val configuration: HistoryConfiguration,
    private val eventBus: EventBus,
    private val history: HistoryRepository
) : Managed {

    val logger by logger()

    @Subscribe
    fun onEvent(event: HistoryMetricQueryEvent) {
        history.record(event.load().asHistorySystemLoad())
        history.purge(configuration.purging.olderThan, configuration.purging.unit)
    }

    override fun start() {
        eventBus.register(this)
    }

    override fun stop() {
        eventBus.unregister(this)
    }

    fun getHistory(): List<SystemHistoryEntry> {
        return getHistoryLimitedToDates(null, null)
    }

    fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<SystemHistoryEntry> {
        val result = measureTimeMillis {
            if (fromDate == null || toDate == null) {
                history.get()
            } else {
                history.getHistoryLimitedToDates(fromDate, toDate)
            }
        }
        logger.info(
            "Took {} to fetch {} history entries",
            "${result.first.toInt()}ms",
            result.second.size
        )
        return result.second
    }

    private fun com.krillsson.sysapi.core.domain.system.SystemLoad.asHistorySystemLoad(): HistorySystemLoad {
        return HistorySystemLoad(
            uptime,
            systemLoadAverage,
            cpuLoad,
            networkInterfaceLoads,
            connectivity,
            driveLoads,
            memory,
            gpuLoads,
            motherboardHealth
        )
    }
}