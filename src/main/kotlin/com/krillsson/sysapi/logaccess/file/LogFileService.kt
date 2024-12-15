package com.krillsson.sysapi.logaccess.file

import com.krillsson.sysapi.graphql.domain.LogMessageConnection
import com.krillsson.sysapi.graphql.domain.LogMessageEdge
import com.krillsson.sysapi.graphql.domain.PageInfo
import com.krillsson.sysapi.logaccess.LogLineParser
import com.krillsson.sysapi.logaccess.LogMessage
import com.krillsson.sysapi.util.lineCount
import com.krillsson.sysapi.util.logger
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


@Service
class LogFileService(private val logLineParser: LogLineParser) {

    val logger by logger()

    fun getLogs(
        logFilePath: String,
        after: String?,
        before: String?,
        first: Int?,
        last: Int?,
        reverse: Boolean?,
    ): LogMessageConnection {
        val allLogs = Files.readAllLines(Paths.get(logFilePath)).let { if (reverse == true) it.reversed() else it }

        // Convert "cursor" into a line index.
        val afterLine = after?.let { decodeCursor(it) } ?: -1
        val beforeLine = before?.let { decodeCursor(it) } ?: allLogs.size

        // Filter logs based on the cursors.
        val filteredLogs = allLogs.subList((afterLine + 1).coerceAtLeast(0), beforeLine.coerceAtMost(allLogs.size))

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
            LogMessageEdge(cursor = encodeCursor(startIndex + index), node = logLineParser.parseLine(logLine))
        }


        logger.debug("Returning ${paginatedLogs.size} from startIndex: $startIndex to endIndex: $endIndex. afterLine: $afterLine beforeLine: $beforeLine first: $first last: $last reversed: $reverse file: $logFilePath")
        return LogMessageConnection(edges = edges, pageInfo = PageInfo(hasNextPage = endIndex < allLogs.lastIndex, hasPreviousPage = startIndex > 0, startCursor = edges.firstOrNull()?.cursor, endCursor = edges.lastOrNull()?.cursor))
    }

    fun tailLogFile(path: String, startPosition: String?, reverse: Boolean?): Flux<LogMessage> {
        return Flux.create { emitter ->
            val logFile = Paths.get(path).toFile()
            val lastIndex = (logFile.lineCount() - 1).coerceAtLeast(0)

            val decodedStartPosition = startPosition?.let { decodeCursor(it).toLong() }

            val historicalLines = if (reverse == true && decodedStartPosition != null && decodedStartPosition in 0..lastIndex) {
                readLineFromStartUntilPosition(logFile, decodedStartPosition)
            } else if (decodedStartPosition in 0..lastIndex && decodedStartPosition != null) {
                readLinesFromPositionUntilEnd(logFile, decodedStartPosition)
            } else {
                emptyList()
            }
            historicalLines.forEach { line ->
                val message = logLineParser.parseLine(line)
                emitter.next(message)
            }
            logger.debug("Returning ${historicalLines.size} from startPosition: $decodedStartPosition reversed: $reverse file: $path")

            val listener = object : TailerListenerAdapter() {
                override fun handle(line: String) {
                    val message = logLineParser.parseLine(line)
                    emitter.next(message)
                }

                override fun fileRotated() {

                }

                override fun handle(ex: Exception) {
                    logger.error("Error while tailing $path", ex)
                    emitter.error(ex) // Handle errors in tailing
                }
            }

            val tailer = Tailer.builder().setPath(path).setCharset(StandardCharsets.UTF_8).setDelayDuration(Duration.ofMillis(500)).setTailerListener(listener).setTailFromEnd(true).setStartThread(true).get()


            emitter.onDispose {
                tailer.close()
            }
        }
    }

    private fun readLinesFromPositionUntilEnd(file: File, startPosition: Long): List<String> {
        val lines = mutableListOf<String>()
        file.bufferedReader(StandardCharsets.UTF_8).useLines { sequence ->
            sequence.drop(startPosition.toInt()).forEach { line -> lines.add(line) }
        }
        return lines
    }

    private fun readLineFromStartUntilPosition(file: File, endPosition: Long): List<String> {
        val lines = mutableListOf<String>()
        file.bufferedReader(StandardCharsets.UTF_8).useLines { sequence ->
            sequence.take(endPosition.toInt()).forEach { line -> lines.add(line) }
        }
        return lines
    }

    private fun encodeCursor(lineIndex: Int): String = Base64.getEncoder().encodeToString(lineIndex.toString().toByteArray())

    private fun decodeCursor(cursor: String): Int = String(Base64.getDecoder().decode(cursor)).toIntOrNull() ?: -1
}
