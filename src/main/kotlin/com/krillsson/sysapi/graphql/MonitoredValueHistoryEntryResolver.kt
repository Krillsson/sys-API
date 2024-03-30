package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.graphql.domain.MonitoredValueHistoryEntry
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping("MonitoredValueHistoryEntry")
class MonitoredValueHistoryEntryResolver {
    @SchemaMapping
    fun date(monitoredValueHistoryEntry: MonitoredValueHistoryEntry) = monitoredValueHistoryEntry.date()

    @SchemaMapping
    fun dateTime(monitoredValueHistoryEntry: MonitoredValueHistoryEntry) = monitoredValueHistoryEntry.dateTime()
}