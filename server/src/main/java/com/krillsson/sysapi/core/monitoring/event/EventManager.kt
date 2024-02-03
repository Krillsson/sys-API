package com.krillsson.sysapi.core.monitoring.event

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.util.Clock
import io.dropwizard.lifecycle.Managed
import org.slf4j.LoggerFactory
import java.util.*

class EventManager(private val repository: EventRepository, private val clock: Clock) : Managed {

    private lateinit var events: MutableList<Event>

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventManager::class.java)
    }

    override fun start() {
        restore()
    }

    override fun stop() {
        endOngoingEvents()
        persist()
    }

    fun add(event: Event) {
        removeOngoingEventForMonitor(event.monitorId)
        events.add(event)
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

    private fun persist() = repository.write(events)

    private fun restore() {
        events = repository.read().toMutableList()
    }

    private fun removeOngoingEventForMonitor(monitorId: UUID) {
        events.removeIf { ongoingEvent ->
            ongoingEvent is OngoingEvent && monitorId == ongoingEvent.monitorId
        }
    }

    private fun endOngoingEvents() {
        val newEvents: MutableList<Event> = mutableListOf()
        events.forEach { event ->
            if (event is OngoingEvent) {
                LOGGER.debug("Shutting down - Ending ongoing event ${event.monitorType} (${event.id})")
                newEvents += PastEvent(
                    event.id,
                    event.monitorId,
                    event.monitoredItemId,
                    event.startTime,
                    clock.instant(),
                    event.monitorType,
                    event.threshold,
                    event.value,
                    event.value
                )
            } else {
                newEvents += event
            }
        }
        events = newEvents
    }
}