package com.krillsson.sysapi.core.history

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile


class HistoryStore(objectMapper: ObjectMapper) :
    JsonFile<List<StoredSystemHistoryEntry>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {
    companion object {
        val MONITORS_TYPE_REFERENCE: TypeReference<List<StoredSystemHistoryEntry>> =
            object : TypeReference<List<StoredSystemHistoryEntry>>() {}

        const val fileName = "history.json"
    }
}