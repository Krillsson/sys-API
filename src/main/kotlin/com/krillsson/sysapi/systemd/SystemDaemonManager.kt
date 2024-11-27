package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.graphql.domain.*
import com.krillsson.sysapi.util.decodeAsInstantCursor
import com.krillsson.sysapi.util.encodeAsCursor
import com.krillsson.sysapi.util.logger
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class SystemDaemonManager(
    mapper: ObjectMapper,
    private val config: YAMLConfigFile
) : SystemDaemonAccessAvailable {

    sealed class Status {
        object Available : Status()
        object Disabled : Status()
        data class Unavailable(val error: RuntimeException) : Status()
    }

    val logger by logger()

    private val journalCtl = JournalCtl(mapper, config.linux.journalLogs)
    private val systemCtl = SystemCtl(mapper, config.linux.systemDaemonServiceManagement)

    private val supportedBySystem = journalCtl.supportedBySystem() && systemCtl.supportedBySystem()

    fun status(): Status {
        return when {
            !config.linux.systemDaemonServiceManagement.enabled -> Status.Disabled
            !supportedBySystem() -> Status.Unavailable(RuntimeException("systemctl or journalctl command was not found"))
            else -> Status.Available
        }
    }

    override fun openJournal(name: String, limit: Int): List<SystemDaemonJournalEntry> {
        return if (config.linux.journalLogs.enabled) {
            journalCtl.lines(name, limit)
        } else {
            emptyList()
        }
    }

    override fun openJournalConnection(
        name: String,
        after: String?,
        before: String?,
        first: Int?,
        last: Int?
    ): SystemDaemonJournalEntryConnection {
        val beforeTimestamp = before?.decodeAsInstantCursor()
        val afterTimestamp = after?.decodeAsInstantCursor()

        val filteredLogs = journalCtl.lines(
            serviceUnitName = name,
            since = afterTimestamp,
            until = beforeTimestamp
        )

        val paginatedLogs = if (first != null) {
            filteredLogs.take(first)
        } else if (last != null) {
            filteredLogs.takeLast(last)
        } else {
            filteredLogs.take(10) // Default page size.
        }

        val edges = paginatedLogs.map {
            SystemDaemonJournalEntryEdge(
                cursor = it.timestamp.encodeAsCursor(),
                node = it
            )
        }

        return SystemDaemonJournalEntryConnection(
            edges = edges,
            pageInfo = PageInfo(
                hasNextPage = filteredLogs.size > paginatedLogs.size,
                hasPreviousPage = before != null || after != null,
                startCursor = edges.firstOrNull()?.cursor,
                endCursor = edges.lastOrNull()?.cursor
            )
        )
    }

    override fun services(): List<SystemCtl.ListServicesOutput.Item> {
        return systemCtl.services()
    }

    override fun serviceDetails(name: String): SystemCtl.ServiceDetailsOutput? {
        return systemCtl.serviceDetails(name)
    }

    fun performCommandWithService(serviceName: String, command: SystemDaemonCommand): CommandResult {
        return systemCtl.performCommandWithService(serviceName, command)
    }

    private fun supportedBySystem(): Boolean {
        return supportedBySystem
    }
}
