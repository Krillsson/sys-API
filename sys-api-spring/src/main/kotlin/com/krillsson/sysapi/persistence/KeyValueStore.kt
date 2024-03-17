package com.krillsson.sysapi.persistence

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class KeyValueStore(objectMapper: ObjectMapper) :
    JsonFile<Map<String, String>>(fileName, TYPE_REFERENCE, objectMapper) {
    companion object {
        val TYPE_REFERENCE: TypeReference<Map<String, String>> =
            object : TypeReference<Map<String, String>>() {}

        const val fileName = "key_values.json"
    }
}