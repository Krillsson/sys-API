package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.PastEvent
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.graphql.domain.Monitor
import com.krillsson.sysapi.graphql.domain.MonitoredValue
import com.krillsson.sysapi.graphql.domain.asMonitor
import com.krillsson.sysapi.graphql.domain.asMonitoredValue
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class PastEventEventResolver(val monitorManager: MonitorManager) : GraphQLResolver<PastEvent> {
    fun getMonitor(event: PastEvent): Monitor {
        return requireNotNull(
            monitorManager.getById(event.monitorId)?.asMonitor()
        ) { "No monitor of type ${event.monitorType} with id ${event.monitorId}" }
    }

    fun getEndValue(event: PastEvent): MonitoredValue {
        return event.value.asMonitoredValue()
    }

    fun getStartValue(event: PastEvent): MonitoredValue {
        return event.startValue.asMonitoredValue()
    }

    fun startTimestamp(event: PastEvent): Instant {
        return event.startTime
    }

    fun endTimestamp(event: PastEvent): Instant {
        return event.endTime
    }
}