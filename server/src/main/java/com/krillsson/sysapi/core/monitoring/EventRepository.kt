package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.persistence.Store

class EventRepository(private val store: Store<List<EventStore.StoredEvent>>) {

    fun read(): List<Event> {
        return store.read()?.map { it.asEvent() }.orEmpty()
    }

    fun write(value: List<Event>) {
        store.write(value.map { it.asStoredEvent() })
    }

    private fun EventStore.StoredEvent.asEvent(): Event {
        return when (type) {
            EventStore.StoredEvent.Type.ONGOING -> OngoingEvent(
                id,
                monitorId,
                monitoredItemId,
                monitorType,
                startTime,
                threshold,
                value
            )
            EventStore.StoredEvent.Type.PAST -> PastEvent(
                id,
                monitorId,
                monitoredItemId,
                startTime,
                requireNotNull(endTime) { "endTime is required for past events" },
                monitorType,
                threshold,
                value
            )
        }
    }

    private fun Event.asStoredEvent(): EventStore.StoredEvent {
        return when (this) {
            is PastEvent -> EventStore.StoredEvent(
                id,
                monitorId,
                monitoredItemId,
                startTime,
                endTime,
                monitorType,
                threshold,
                value,
                EventStore.StoredEvent.Type.PAST
            )
            is OngoingEvent -> EventStore.StoredEvent(
                id,
                monitorId,
                monitoredItemId,
                startTime,
                null,
                monitorType,
                threshold,
                value,
                EventStore.StoredEvent.Type.ONGOING
            )
            else -> throw IllegalArgumentException("Unknown event type encountered $this")
        }
    }
}