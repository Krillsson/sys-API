package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.domain.Monitor
import com.krillsson.sysapi.graphql.domain.MonitoredValue
import com.krillsson.sysapi.graphql.domain.asMonitor
import com.krillsson.sysapi.graphql.domain.asMonitoredValue
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.time.Instant

@Controller
@SchemaMapping(typeName = "PastEvent")
class PastEventResolver(val monitorManager: MonitorManager) {
    @SchemaMapping(typeName="PastEvent", field="monitor")
    fun monitor(event: PastEvent): Monitor {
        return requireNotNull(
                monitorManager.getById(event.monitorId)?.asMonitor()
        ) { "No monitor of type ${event.monitorType} with id ${event.monitorId}" }
    }

    @SchemaMapping
    fun endValue(event: PastEvent): MonitoredValue {
        return event.value.asMonitoredValue()
    }

    @SchemaMapping(typeName="PastEvent", field="startValue")
    fun startValue(event: PastEvent): MonitoredValue {
        return event.startValue.asMonitoredValue()
    }

    @SchemaMapping(typeName="PastEvent", field="startTimestamp")
    fun startTimestamp(event: PastEvent): Instant {
        return event.startTime
    }

    @SchemaMapping
    fun endTimestamp(event: PastEvent): Instant {
        return event.endTime
    }
}