package com.krillsson.sysapi.logaccess.file

import com.krillsson.sysapi.graphql.domain.LogMessageConnection
import com.krillsson.sysapi.graphql.domain.LogMessageEdge
import com.krillsson.sysapi.graphql.domain.PageInfo
import com.krillsson.sysapi.logaccess.LogLineParser
import com.krillsson.sysapi.logaccess.LogMessage
import org.apache.commons.io.input.Tailer
import org.apache.commons.io.input.TailerListenerAdapter
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean


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

            // Decode the `after` parameter or default to the current file length
            val startPosition = after?.let { decodeCursor(it).toLong() } ?: 0L

            // Emit historical lines from the after position
            if (startPosition > 0) {
                val historicalLines = readLinesFromPosition(logFile, startPosition)
                historicalLines.forEach { emitter.next(logLineParser.parseLine(it)) }
            }

            val running = AtomicBoolean(true)

            val listener = object : TailerListenerAdapter() {
                override fun handle(line: String) {
                    if (running.get()) {
                        emitter.next(logLineParser.parseLine(line)) // Emit new log lines
                    }
                }

                override fun handle(ex: Exception) {
                    emitter.error(ex) // Handle errors in tailing
                }
            }

            val tailer = Tailer.builder()
                .setPath(path)
                .setCharset(StandardCharsets.UTF_8)
                .setDelayDuration(Duration.ofMillis(500))
                .setTailerListener(listener)
                .setTailFromEnd(true)
                .setStartThread(true)
                .get()


            emitter.onDispose {
                running.set(false)
                tailer.close()
            }
        }
    }

    private fun readLinesFromPosition(file: File, startPosition: Long): List<String> {
        val lines = mutableListOf<String>()
        file.inputStream().use { stream ->
            stream.channel.position(startPosition)
            stream.reader(StandardCharsets.UTF_8).useLines { sequence ->
                sequence.forEach { line -> lines.add(line) }
            }
        }
        return lines
    }

    private fun encodeCursor(lineIndex: Int): String =
        Base64.getEncoder().encodeToString(lineIndex.toString().toByteArray())

    private fun decodeCursor(cursor: String): Int =
        String(Base64.getDecoder().decode(cursor)).toIntOrNull() ?: -1
}
