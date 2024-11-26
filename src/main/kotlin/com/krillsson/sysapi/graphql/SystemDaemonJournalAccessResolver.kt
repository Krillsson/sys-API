package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessAvailable
import com.krillsson.sysapi.graphql.domain.SystemDaemonJournalEntryConnection
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class SystemDaemonJournalAccessResolver {
    @SchemaMapping
    fun services(systemDaemonAccessAvailable: SystemDaemonAccessAvailable) = systemDaemonAccessAvailable.services()

    @SchemaMapping
    fun openJournal(systemDaemonAccessAvailable: SystemDaemonAccessAvailable, @Argument serviceName: String, @Argument limit: Int) = systemDaemonAccessAvailable.openJournal(serviceName, limit)

    @SchemaMapping
    fun serviceDetails(systemDaemonAccessAvailable: SystemDaemonAccessAvailable, @Argument serviceName: String) = systemDaemonAccessAvailable.serviceDetails(serviceName)

    @SchemaMapping
    fun openJournalConnection(
        systemDaemonAccessAvailable: SystemDaemonAccessAvailable,
        @Argument serviceName: String,
        @Argument after: String?,
        @Argument before: String?,
        @Argument first: Int?,
        @Argument last: Int?
    ): SystemDaemonJournalEntryConnection {
        return systemDaemonAccessAvailable.openJournalConnection(
            name = serviceName,
            after = after,
            before = before,
            first = first,
            last = last
        )
    }
}