package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.persistence.JsonFile

class EventStore(objectMapper: ObjectMapper) : JsonFile<List<Event>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {
    companion object {
        val MONITORS_TYPE_REFERENCE: TypeReference<List<Event>> =
                object : TypeReference<List<Event>>() {}

        const val fileName = "events.json"
    }
}