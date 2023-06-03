package com.krillsson.sysapi.core.genericevents

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile

class GenericEventStore(objectMapper: ObjectMapper) :
    JsonFile<List<GenericEvent>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {

    companion object {
        val MONITORS_TYPE_REFERENCE: TypeReference<List<GenericEvent>> =
            object : TypeReference<List<GenericEvent>>() {}

        const val fileName = "generic_events.json"
    }
}