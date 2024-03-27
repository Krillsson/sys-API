package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.graphql.domain.MonitorEvent
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class MonitorEventResolver : GraphQLResolver<MonitorEvent> {
    fun getType(monitorEvent: MonitorEvent) = monitorEvent.monitorType
}