package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.util.toOffsetDateTime
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class MonitoredItemMissingGenericEventResolver : GraphQLResolver<GenericEvent.MonitoredItemMissing> {
    fun getTitle(event: GenericEvent.MonitoredItemMissing): String {
        return "Monitored item is missing"
    }

    fun getDescription(event: GenericEvent.MonitoredItemMissing): String {
        return "${event.monitorType.name} monitor's item ${event.monitoredItemId} is no longer present in the system"
    }

    fun dateTime(event: GenericEvent.MonitoredItemMissing) = event.timestamp.toOffsetDateTime()
}