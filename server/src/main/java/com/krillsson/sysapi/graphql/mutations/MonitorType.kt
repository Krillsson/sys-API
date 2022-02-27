package com.krillsson.sysapi.graphql.mutations

import com.krillsson.sysapi.core.monitoring.Monitor

enum class NumericalValueMonitorType {
    CPU_TEMP,
    DRIVE_SPACE,
    DRIVE_READ_RATE,
    DRIVE_WRITE_RATE,
    MEMORY_SPACE,
    NETWORK_UPLOAD_RATE,
    NETWORK_DOWNLOAD_RATE,
    PROCESS_MEMORY_SPACE;

    fun toMonitorType() = requireNotNull(
        Monitor.Type.values()
            .firstOrNull { name == it.name }) { "No equivalent to $name exists in ${Monitor.Type::class.simpleName}" }
}

enum class FractionalValueMonitorType {
    CPU_LOAD,
    PROCESS_CPU_LOAD;

    fun toMonitorType() = requireNotNull(
        Monitor.Type.values()
            .firstOrNull { name == it.name }) { "No equivalent to $name exists in ${Monitor.Type::class.simpleName}" }
}

enum class BooleanValueMonitorType {
    NETWORK_UP,
    CONTAINER_RUNNING,
    PROCESS_EXISTS,
    CONNECTIVITY,
    EXTERNAL_IP_CHANGED;

    fun toMonitorType() = requireNotNull(
        Monitor.Type.values()
            .firstOrNull { name == it.name }) { "No equivalent to $name exists in ${Monitor.Type::class.simpleName}" }
}