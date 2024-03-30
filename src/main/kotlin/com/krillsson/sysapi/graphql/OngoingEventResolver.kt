package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.domain.Monitor
import com.krillsson.sysapi.graphql.domain.MonitoredValue
import com.krillsson.sysapi.graphql.domain.asMonitor
import com.krillsson.sysapi.graphql.domain.asMonitoredValue
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.time.Instant

@Controller
@SchemaMapping(typeName = "OngoingEvent")
class OngoingEventResolver(val monitorManager: MonitorManager) {
    @SchemaMapping(typeName = "OngoingEvent", field = "monitor")
    fun monitor(event: OngoingEvent): Monitor {
        return requireNotNull(
                monitorManager.getById(event.monitorId)?.asMonitor()
        ) { "No monitor of type ${event.monitorType} with id ${event.monitorId}" }
    }

    @SchemaMapping(typeName = "OngoingEvent", field = "startValue")
    fun startValue(event: OngoingEvent): MonitoredValue {
        return event.value.asMonitoredValue()
    }

    @SchemaMapping(typeName = "OngoingEvent", field = "startTimestamp")
    fun startTimestamp(event: OngoingEvent): Instant {
        return event.startTime
    }
}