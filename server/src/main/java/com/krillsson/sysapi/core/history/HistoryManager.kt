package com.krillsson.sysapi.core.history

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.domain.system.SystemLoad
import io.dropwizard.lifecycle.Managed
import java.time.OffsetDateTime
import java.util.stream.Collectors

open class HistoryManager @JvmOverloads constructor(
    private val configuration: HistoryConfiguration,
    private val eventBus: EventBus,
    private val history: History<SystemLoad> = History()
) : Managed {
    @Subscribe
    fun onEvent(event: HistoryMetricQueryEvent) {
        history.record(event.load())
        history.purge(configuration.purging.olderThan, configuration.purging.unit)
    }

    override fun start() {
        eventBus.register(this)
    }

    override fun stop() {
        eventBus.unregister(this)
    }

    fun getHistory(): List<HistoryEntry<SystemLoad>> {
        return history.get()
    }

    fun getHistoryLimitedToDates(
        fromDate: OffsetDateTime?,
        toDate: OffsetDateTime?
    ): List<HistoryEntry<SystemLoad>> {
        return history.getHistoryLimitedToDates(fromDate, toDate)
    }
}