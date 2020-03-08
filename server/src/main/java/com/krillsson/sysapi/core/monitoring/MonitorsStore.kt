package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile
import java.util.*

class MonitorsStore(objectMapper: ObjectMapper) : JsonFile<HashMap<String, MonitorInput>>(fileName, monitorsTypeReference,
        HashMap<String, MonitorInput>(),
        objectMapper) {


    companion object {
        val monitorsTypeReference: TypeReference<HashMap<String, MonitorInput>> =
                object : TypeReference<HashMap<String, MonitorInput>>() {}

        const val fileName = "monitors.json"
    }
}