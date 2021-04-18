package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.persistence.Store
import io.dropwizard.lifecycle.Managed
import org.slf4j.LoggerFactory
import java.util.*

class EventManager(private val store: Store<List<Event>>) : Managed {

    private lateinit var events: MutableList<Event>

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventManager::class.java)
    }

    override fun start() {
        restore()
    }

    override fun stop() {
        persist()
    }

    fun add(event: Event) {
        when (event) {
            is PastEvent -> {
                events.removeIf { ongoingEvent -> event.id == ongoingEvent.id }
                events.add(event)
            }
            is OngoingEvent -> events.add(event)
        }
        persist()
    }

    fun getAll(): List<Event> = events

    fun remove(id: UUID): Boolean = events
        .removeAll { it.id == id }
        .also { persist() }

    fun eventsForMonitorId(id: UUID): List<Event> = events.filter { it.monitorId == id }

    fun removeEventsForMonitorId(id: UUID) = events
        .removeAll { it.monitorId == id }
        .also { persist() }

    private fun persist() = store.write(events)

    private fun restore() {
        events = store.read().orEmpty().toMutableList()
    }
}