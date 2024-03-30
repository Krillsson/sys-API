package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.event.Event
import com.krillsson.sysapi.core.monitoring.MonitorManager
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "Event")
class EventResolver(val monitorManager: MonitorManager) {
    @SchemaMapping
    fun monitor(event: Event) = monitorManager.getById(event.monitorId)

    @SchemaMapping
    fun startTimestamp(event: Event) = event.startTime
}