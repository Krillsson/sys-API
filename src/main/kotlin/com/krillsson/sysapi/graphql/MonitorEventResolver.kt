package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.graphql.domain.MonitorEvent
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class MonitorEventResolver {

    @SchemaMapping
    fun type(monitorEvent: MonitorEvent) = monitorEvent.monitorType
}