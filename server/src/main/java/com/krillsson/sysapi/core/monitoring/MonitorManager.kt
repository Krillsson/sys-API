package com.krillsson.sysapi.core.monitoring

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorFactory.createMonitor
import com.krillsson.sysapi.persistence.Store
import com.krillsson.sysapi.util.Clock
import io.dropwizard.lifecycle.Managed
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

class MonitorManager(private val eventManager: EventManager, private val eventBus: EventBus, private val store: Store<List<Monitor>>, private val provider: Metrics, private val clock: Clock) : Managed {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MonitorManager::class.java)
    }

    private lateinit var activeMonitors: MutableMap<UUID, Pair<MonitorMechanism, Monitor>>


    @Subscribe
    fun onEvent(metricQueryEvent: MonitorMetricQueryEvent) {
        activeMonitors.values.forEach { (mechanism, monitor) ->
            val event = mechanism.check(metricQueryEvent.load(), monitor)
            event?.let {
                eventManager.add(it)
            }
        }
    }

    override fun start() {
        restore()
        eventBus.register(this)
    }

    override fun stop() {
        eventBus.unregister(this)
        persist()
    }

    fun getAll() = activeMonitors.map { it.value.second }

    fun getById(id: UUID): Monitor? {
        return activeMonitors[id]?.second
    }

    fun add(inertia: Duration, type: MonitorType, threshold: Double, itemId: String?): UUID? {
        val config = MonitorConfig(itemId, threshold, inertia)
        val monitor = createMonitor(type, UUID.randomUUID(), config)
        return if (validate(monitor)) {
            register(monitor)
            persist()
            monitor.id
        } else {
            throw IllegalArgumentException("Not mappable to device: $type with $itemId")
        }
    }

    fun remove(id: UUID): Boolean {
        val removed = activeMonitors.remove(id)

        removed?.let {
            persist()
            eventManager.removeEventsForMonitorId(id)
        }
        return removed != null
    }

    private fun persist() {
        val monitors = activeMonitors.map { it.value.second }.toList()
        store.write(monitors)
    }

    private fun restore() {
        activeMonitors = mutableMapOf()
        store.read().orEmpty().forEach { monitor ->
            register(monitor)
        }
    }

    private fun register(monitor: Monitor) {
        val mechanism = MonitorMechanism(clock)
        activeMonitors[monitor.id] = mechanism to monitor
    }

    private fun validate(monitor: Monitor): Boolean {
        return monitor.selectValue(provider.consolidatedMetrics()) != -1.0
    }
}

