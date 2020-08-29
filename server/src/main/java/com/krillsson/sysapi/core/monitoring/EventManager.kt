package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.event.EventType
import com.krillsson.sysapi.core.domain.event.MonitorEvent
import com.krillsson.sysapi.persistence.Store
import io.dropwizard.lifecycle.Managed
import org.slf4j.LoggerFactory
import java.util.*

class EventManager(private val store: Store<List<MonitorEvent>>) : Managed {

    private lateinit var events: MutableList<MonitorEvent>

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventManager::class.java)
    }

    override fun start() {
        restore()
    }

    override fun stop() {
        persist()
    }

    fun add(event: MonitorEvent) {
        if (event.eventType == EventType.STOP && getAll().stream()
                        .noneMatch { me: MonitorEvent -> me.id == event.id }) {
            LOGGER.warn("Received STOP event for explicitly removed event, ignoring...")
        } else {
            events.add(event)
        }
    }

    fun getAll(): List<MonitorEvent> = events

    fun remove(id: UUID): Boolean = events
            .removeAll { it.id == id }
            .also { persist() }

    fun eventsForMonitorId(id: UUID): List<MonitorEvent> = events.filter { it.monitorId == id }

    fun removeEventsForMonitorId(id: UUID) = events
            .removeAll { it.monitorId == id }
            .also { persist() }

    private fun persist() = store.write(events)

    private fun restore() {
        events = store.read().orEmpty().toMutableList()
    }
}