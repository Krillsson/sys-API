package com.krillsson.sysapi.logaccess.file

import com.krillsson.sysapi.graphql.domain.LogMessageConnection
import com.krillsson.sysapi.graphql.domain.LogMessageEdge
import com.krillsson.sysapi.graphql.domain.PageInfo
import com.krillsson.sysapi.logaccess.LogLineParser
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class LogFileService(private val logLineParser: LogLineParser) {

    fun getLogs(
        logFilePath: String,
        after: String?,
        before: String?,
        first: Int?,
        last: Int?
    ): LogMessageConnection {
        val allLogs = Files.readAllLines(Paths.get(logFilePath))

        // Convert "cursor" into a line index.
        val afterLine = after?.let { decodeCursor(it) } ?: -1
        val beforeLine = before?.let { decodeCursor(it) } ?: allLogs.size

        // Filter logs based on the cursors.
        val filteredLogs = allLogs.subList(
            (afterLine + 1).coerceAtLeast(0),
            beforeLine.coerceAtMost(allLogs.size)
        )

        // Apply pagination (first or last).
        val paginatedLogs = if (first != null) {
            filteredLogs.take(first)
        } else if (last != null) {
            filteredLogs.takeLast(last)
        } else {
            filteredLogs.take(10) // Default page size.
        }

        val startIndex = allLogs.indexOf(paginatedLogs.firstOrNull())
        val endIndex = allLogs.indexOf(paginatedLogs.lastOrNull())

        val edges = paginatedLogs.mapIndexed { index, logLine ->
            LogMessageEdge(
                cursor = encodeCursor(startIndex + index),
                node = logLineParser.parseLine(logLine)
            )
        }

        return LogMessageConnection(
            edges = edges,
            pageInfo = PageInfo(
                hasNextPage = endIndex < allLogs.lastIndex,
                hasPreviousPage = startIndex > 0,
                startCursor = edges.firstOrNull()?.cursor,
                endCursor = edges.lastOrNull()?.cursor
            )
        )
    }

    private fun encodeCursor(lineIndex: Int): String =
        Base64.getEncoder().encodeToString(lineIndex.toString().toByteArray())

    private fun decodeCursor(cursor: String): Int =
        String(Base64.getDecoder().decode(cursor)).toIntOrNull() ?: -1
}
