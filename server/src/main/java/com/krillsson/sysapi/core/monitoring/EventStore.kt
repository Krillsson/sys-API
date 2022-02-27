package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile
import java.time.OffsetDateTime
import java.util.*

class EventStore(objectMapper: ObjectMapper) :
    JsonFile<List<EventStore.StoredEvent>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {

    data class StoredEvent(
        val id: UUID,
        val monitorId: UUID,
        val monitoredItemId: String?,
        val startTime: OffsetDateTime,
        val endTime: OffsetDateTime?,
        val monitorType: Monitor.Type,
        val threshold: Double,
        val value: Double,
        val type: Type
    ) {
        enum class Type {
            ONGOING,
            PAST
        }
    }


    companion object {
        @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
        val MONITORS_TYPE_REFERENCE: TypeReference<List<StoredEvent>> =
            object : TypeReference<List<StoredEvent>>() {}

        const val fileName = "events.json"
    }
}