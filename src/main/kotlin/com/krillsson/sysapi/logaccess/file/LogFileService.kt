package com.krillsson.sysapi.logaccess.file

import com.krillsson.sysapi.graphql.domain.LogMessageConnection
import com.krillsson.sysapi.graphql.domain.LogMessageEdge
import com.krillsson.sysapi.graphql.domain.PageInfo
import com.krillsson.sysapi.logaccess.LogLineParser
import com.krillsson.sysapi.logaccess.LogMessage
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.atomic.AtomicLong


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

    fun tailLogFile(path: String, after: String?): Flux<LogMessage> {
        return Flux.create { emitter ->
            val logFile = Paths.get(path).toFile()
            val startPosition = after?.let { decodeCursor(after).toLong() } ?: logFile.length()
            val lastReadPosition = AtomicLong(startPosition)

            val observer = FileAlterationObserver(logFile.parentFile)
            val listener = object : FileAlterationListenerAdaptor() {
                override fun onFileChange(file: File) {
                    if (file == logFile) {
                        val newLength = file.length()
                        if (newLength > lastReadPosition.get()) {
                            val newLines = readLinesFromPosition(file, lastReadPosition.get(), newLength)
                            newLines.forEach { emitter.next(logLineParser.parseLine(it)) }
                            lastReadPosition.set(newLength) // Update the position
                        }
                    }
                }
            }

            observer.addListener(listener)

            val monitor = FileAlterationMonitor(500, observer)
            monitor.start()

            emitter.onDispose {
                try {
                    monitor.stop()
                } catch (e: Exception) {
                    emitter.error(e)
                }
            }
        }
    }

    private fun readLinesFromPosition(file: File, startPosition: Long, endPosition: Long): List<String> {
        val lines = mutableListOf<String>()
        file.inputStream().use { stream ->
            stream.channel.position(startPosition)
            stream.reader(StandardCharsets.UTF_8).useLines { sequence ->
                sequence.forEach { line ->
                    if (stream.channel.position() <= endPosition) {
                        lines.add(line)
                    }
                }
            }
        }
        return lines
    }

    private fun encodeCursor(lineIndex: Int): String =
        Base64.getEncoder().encodeToString(lineIndex.toString().toByteArray())

    private fun decodeCursor(cursor: String): Int =
        String(Base64.getDecoder().decode(cursor)).toIntOrNull() ?: -1
}
