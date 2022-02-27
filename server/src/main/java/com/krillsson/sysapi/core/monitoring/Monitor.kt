package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import java.util.*

abstract class Monitor<out T : MonitoredValue> {
    abstract val id: UUID
    abstract val type: Type
    abstract val config: MonitorConfig<out T>

    enum class Type {
        CPU_LOAD,
        CPU_TEMP,
        DRIVE_SPACE,
        DRIVE_READ_RATE,
        DRIVE_WRITE_RATE,
        MEMORY_SPACE,
        NETWORK_UP,
        NETWORK_UPLOAD_RATE,
        NETWORK_DOWNLOAD_RATE,
        CONTAINER_RUNNING,
        PROCESS_MEMORY_SPACE,
        PROCESS_CPU_LOAD,
        PROCESS_EXISTS,
        CONNECTIVITY,
        EXTERNAL_IP_CHANGED
    }

    abstract fun selectValue(event: MonitorMetricQueryEvent): T?
    abstract fun isPastThreshold(value: @UnsafeVariance T): Boolean
}