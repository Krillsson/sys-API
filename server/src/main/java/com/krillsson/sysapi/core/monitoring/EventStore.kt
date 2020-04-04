package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile

class EventStore(objectMapper: ObjectMapper) : JsonFile<List<MonitorEvent>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {
    companion object {
        val MONITORS_TYPE_REFERENCE: TypeReference<List<MonitorEvent>> =
                object : TypeReference<List<MonitorEvent>>() {}

        const val fileName = "events.json"
    }
}