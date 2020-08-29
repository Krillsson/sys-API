package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile

class MonitorStore(objectMapper: ObjectMapper) : JsonFile<List<Monitor>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {
    companion object {
        val MONITORS_TYPE_REFERENCE: TypeReference<List<Monitor>> =
                object : TypeReference<List<Monitor>>() {}

        const val fileName = "monitors.json"
    }
}