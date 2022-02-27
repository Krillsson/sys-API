package com.krillsson.sysapi.core.monitoring

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorFactory.createMonitor
import com.krillsson.sysapi.util.Clock
import com.krillsson.sysapi.util.logger
import io.dropwizard.lifecycle.Managed
import java.time.Duration
import java.util.*

class MonitorManager(
    private val eventManager: EventManager,
    private val eventBus: EventBus,
    private val repository: MonitorRepository,
    private val provider: Metrics,
    private val clock: Clock
) : Managed {

    companion object {
        val logger by logger()
    }

    private lateinit var activeMonitors: MutableMap<UUID, Pair<MonitorMechanism, Monitor<MonitoredValue>>>

    @Subscribe
    fun onEvent(metricQueryEvent: MonitorMetricQueryEvent) {
        activeMonitors.values.forEach { (mechanism, monitor) ->
            val value = monitor.selectValue(metricQueryEvent)
            if (value != null) {
                val isOverThreshold = monitor.isPastThreshold(value)
                val event = mechanism.check(
                    monitor,
                    monitor.config,
                    value,
                    isOverThreshold
                )
                event?.let {
                    eventManager.add(it)
                }
            } else {
                logger.warn("${monitor.type.name} monitoring ${monitor.config.monitoredItemId} is no longer valid")
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

    fun getById(id: UUID): Monitor<MonitoredValue>? {
        return activeMonitors[id]?.second
    }

    fun add(inertia: Duration, type: Monitor.Type, threshold: MonitoredValue, itemId: String?): UUID {
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

    fun update(monitorId: UUID, inertia: Duration?, threshold: MonitoredValue?): UUID {
        val oldMonitor = getById(monitorId)
        checkNotNull(oldMonitor) { "No monitor with id $monitorId was found" }
        check(inertia != null || threshold != null) { "Either inertia or threshold has to be provided" }
        val updatedConfig = MonitorConfig(
            oldMonitor.config.monitoredItemId,
            threshold ?: oldMonitor.config.threshold,
            inertia ?: oldMonitor.config.inertia
        )
        val updatedMonitor = createMonitor(oldMonitor.type, oldMonitor.id, updatedConfig)
        return if (validate(updatedMonitor)) {
            register(updatedMonitor)
            persist()
            updatedMonitor.id
        } else {
            throw IllegalArgumentException("Not mappable to device: ${oldMonitor.type} with ${oldMonitor.config.monitoredItemId}")
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
        repository.write(monitors)
    }

    private fun restore() {
        activeMonitors = mutableMapOf()
        repository.read().forEach { monitor ->
            register(monitor)
        }
    }

    private fun register(monitor: Monitor<MonitoredValue>) {
        val mechanism = MonitorMechanism(clock)
        activeMonitors[monitor.id] = mechanism to monitor
    }

    private fun validate(monitor: Monitor<MonitoredValue>): Boolean {
        return monitor.selectValue(MonitorMetricQueryEvent(provider.systemMetrics().systemLoad(), emptyList())) != null
    }
}

