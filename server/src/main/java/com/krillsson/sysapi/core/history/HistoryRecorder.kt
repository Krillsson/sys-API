package com.krillsson.sysapi.core.history

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import io.dropwizard.lifecycle.Managed

class HistoryRecorder(
    private val configuration: HistoryConfiguration,
    private val eventBus: EventBus,
    private val history: HistoryRepository
) : Managed {

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

    private fun com.krillsson.sysapi.core.domain.system.SystemLoad.asHistorySystemLoad(): HistorySystemLoad {
        return HistorySystemLoad(
            uptime,
            systemLoadAverage,
            cpuLoad,
            networkInterfaceLoads,
            connectivity,
            driveLoads,
            diskLoads,
            fileSystemLoads,
            memory,
            gpuLoads,
            motherboardHealth
        )
    }
}