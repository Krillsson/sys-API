package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.util.toOffsetDateTime
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "MonitoredItemMissingGenericEvent")
class MonitoredItemMissingGenericEventResolver {
    @SchemaMapping
    fun title(event: GenericEvent.MonitoredItemMissing): String {
        return "Monitored item is missing"
    }

    @SchemaMapping
    fun description(event: GenericEvent.MonitoredItemMissing): String {
        return "${event.monitorType.name} monitor's item ${event.monitoredItemId} is no longer present in the system"
    }

    @SchemaMapping
    fun dateTime(event: GenericEvent.MonitoredItemMissing) = event.timestamp.toOffsetDateTime()
}