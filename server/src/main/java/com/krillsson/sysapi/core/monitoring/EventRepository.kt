package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.domain.monitor.toBooleanValue
import com.krillsson.sysapi.core.domain.monitor.toFractionalValue
import com.krillsson.sysapi.core.domain.monitor.toNumericalValue
import com.krillsson.sysapi.graphql.mutations.BooleanValueMonitorType
import com.krillsson.sysapi.graphql.mutations.FractionalValueMonitorType
import com.krillsson.sysapi.graphql.mutations.NumericalValueMonitorType
import com.krillsson.sysapi.persistence.Store

class EventRepository(private val store: Store<List<EventStore.StoredEvent>>) {

    fun read(): List<Event> {
        return store.read()?.map { it.asEvent() }.orEmpty()
    }

    fun write(value: List<Event>) {
        store.write(value.map { it.asStoredEvent() })
    }

    private fun EventStore.StoredEvent.asEvent(): Event {
        val convertedValue = when {
            BooleanValueMonitorType.values().any { it.name == type.name } -> value.toBooleanValue()
            FractionalValueMonitorType.values().any { it.name == type.name } -> value.toFractionalValue()
            NumericalValueMonitorType.values().any { it.name == type.name } -> value.toNumericalValue()
            else -> throw IllegalStateException("No equivalent to $this exists in ${Monitor.Type::class.simpleName}")
        }
        val convertedThreshold = when {
            BooleanValueMonitorType.values().any { it.name == type.name } -> threshold.toBooleanValue()
            FractionalValueMonitorType.values().any { it.name == type.name } -> threshold.toFractionalValue()
            NumericalValueMonitorType.values().any { it.name == type.name } -> threshold.toNumericalValue()
            else -> throw IllegalStateException("No equivalent to $this exists in ${Monitor.Type::class.simpleName}")
        }
        return when (type) {
            EventStore.StoredEvent.Type.ONGOING -> OngoingEvent(
                id = id,
                monitorId = monitorId,
                monitoredItemId = monitoredItemId,
                monitorType = monitorType,
                startTime = startTime,
                threshold = convertedThreshold,
                value = convertedValue
            )
            EventStore.StoredEvent.Type.PAST -> PastEvent(
                id = id,
                monitorId = monitorId,
                monitoredItemId = monitoredItemId,
                startTime = startTime,
                endTime = requireNotNull(endTime) { "endTime is required for past events" },
                type = monitorType,
                threshold = convertedThreshold,
                value = convertedValue
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
                threshold.toDouble(),
                value.toDouble(),
                EventStore.StoredEvent.Type.PAST
            )
            is OngoingEvent -> EventStore.StoredEvent(
                id,
                monitorId,
                monitoredItemId,
                startTime,
                null,
                monitorType,
                threshold.toDouble(),
                value.toDouble(),
                EventStore.StoredEvent.Type.ONGOING
            )
            else -> throw IllegalArgumentException("Unknown event type encountered $this")
        }
    }
}