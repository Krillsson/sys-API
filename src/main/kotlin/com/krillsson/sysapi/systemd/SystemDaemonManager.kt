package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.graphql.domain.PageInfo
import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessAvailable
import com.krillsson.sysapi.graphql.domain.SystemDaemonJournalEntryConnection
import com.krillsson.sysapi.graphql.domain.SystemDaemonJournalEntryEdge
import com.krillsson.sysapi.util.decodeAsInstantCursor
import com.krillsson.sysapi.util.encodeAsCursor
import com.krillsson.sysapi.util.logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Instant

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
        last: Int?,
        reverse: Boolean?
    ): SystemDaemonJournalEntryConnection {
        val (fromTimestamp, toTimestamp) = if (reverse == true) {
            before?.decodeAsInstantCursor() to after?.decodeAsInstantCursor()
        } else {
            after?.decodeAsInstantCursor() to before?.decodeAsInstantCursor()
        }
        val pageSize = first ?: last ?: 10

        val filteredLogs = when {
            reverse == true && fromTimestamp == null && toTimestamp == null -> {
                journalCtl.lines(
                    name,
                    limit = pageSize
                )
                    .sortedByDescending { it.timestamp }
            }

            else -> {
                journalCtl.lines(
                    name,
                    since = fromTimestamp,
                    until = toTimestamp
                )
            }
        }

        val sortedLogs = if (reverse == true) {
            filteredLogs.sortedByDescending { it.timestamp }
        } else {
            filteredLogs
        }

        val paginatedLogs = if (first != null) {
            sortedLogs.take(first)
        } else if (last != null) {
            sortedLogs.takeLast(last)
        } else {
            sortedLogs.take(10) // Default page size.
        }

        val edges = paginatedLogs.map {
            SystemDaemonJournalEntryEdge(
                cursor = it.timestamp.encodeAsCursor(),
                node = it
            )
        }
        val pageInfo = PageInfo(
            hasNextPage = sortedLogs.size > paginatedLogs.size && reverse != true,
            hasPreviousPage = sortedLogs.size > paginatedLogs.size && reverse == true,
            startCursor = edges.firstOrNull()?.cursor,
            endCursor = edges.lastOrNull()?.cursor
        )
        logger.info("Container: $name, after: $after, before: $before, first: $first, last: $last, reverse: $reverse")
        logger.info("Returning info: $pageInfo and ${edges.size} edges")
        return SystemDaemonJournalEntryConnection(
            edges = edges,
            pageInfo = pageInfo
        )
    }

    override fun openAndTailJournal(name: String, startPosition: String?, reverse: Boolean?): Flux<SystemDaemonJournalEntry> {
        val historicalLines = if (startPosition != null) {
            val timestamp = startPosition.decodeAsInstantCursor()
            journalCtl.lines(
                name,
                since = timestamp,
                until = Instant.now()
            )
                .let { list -> if (reverse == true) list.sortedByDescending { it.timestamp } else list }
                .filter { it.timestamp.isAfter(timestamp) }
        } else {
            emptyList()
        }

        return journalCtl
            .tailLines(name)
            .startWith(Flux.fromIterable(historicalLines))
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
