package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.persistence.JsonFile
import java.time.Duration
import java.util.*

class MonitorStore(objectMapper: ObjectMapper) :
    JsonFile<List<MonitorStore.StoredMonitor>>(fileName, MONITORS_TYPE_REFERENCE, objectMapper) {
    companion object {
        val MONITORS_TYPE_REFERENCE: TypeReference<List<StoredMonitor>> =
            object : TypeReference<List<StoredMonitor>>() {}

        const val fileName = "monitors.json"
    }

    data class StoredMonitor(
        val id: UUID,
        val type: Type,
        val config: Config
    ) {
        data class Config(
            val monitoredItemId: String? = null,
            val threshold: Double,
            val inertia: Duration
        )

        enum class Type {
            CPU_LOAD, CPU_TEMP, DRIVE_SPACE, MEMORY_SPACE, NETWORK_UP, CONTAINER_RUNNING, PROCESS_MEMORY_SPACE, PROCESS_CPU_LOAD
        }

    }
}