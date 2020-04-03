package com.krillsson.sysapi.core.monitoring

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.dto.monitor.Monitor
import com.krillsson.sysapi.persistence.Store
import io.dropwizard.lifecycle.Managed
import java.util.*

class MonitorManagerV2(private val eventBus: EventBus, private val store: Store<HashMap<UUID, MonitorInput>>, private val provider: Metrics) : Managed {
    lateinit var activeMonitorInputs: MutableMap<UUID, MonitorInput>
    lateinit var activeMonitors: MutableMap<UUID, Monitor>


    override fun start() {
        activeMonitorInputs = store.read().orEmpty().toMutableMap()
        eventBus.register(this)
    }

    override fun stop() {
        eventBus.unregister(this)
    }

    fun addMonitor(): UUID {

    }

    @Subscribe
    fun onEvent(event: MonitorMetricQueryEvent) {

    }

    private fun validate(monitorInput: MonitorInput): Boolean {

    }

    fun removeMonitor(uuid: UUID): String? {

    }

    fun getMonitorById(id: String?): MonitorInput {

    }
}

class EventManager(private val store: Store<List<MonitorEvent>>) {
    fun events(): List<MonitorEvent> {

    }

    fun removeEvents(id: String?): String? {

    }

    fun eventsForMonitorId(uuid: UUID): List<MonitorEvent> {

    }
}