package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.OngoingEvent
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.domain.Monitor
import com.krillsson.sysapi.graphql.domain.MonitoredValue
import com.krillsson.sysapi.graphql.domain.asMonitor
import com.krillsson.sysapi.graphql.domain.asMonitoredValue
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class OngoingEventResolver(val monitorManager: MonitorManager) : GraphQLResolver<OngoingEvent> {
    fun getMonitor(event: OngoingEvent): Monitor {
        return requireNotNull(
            monitorManager.getById(event.monitorId)?.asMonitor()
        ) { "No monitor of type ${event.monitorType} with id ${event.monitorId}" }
    }

    fun getStartValue(event: OngoingEvent): MonitoredValue {
        return event.value.asMonitoredValue()
    }

    fun startTimestamp(event: OngoingEvent): Instant {
        return event.startTime
    }
}