package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.graphql.domain.LogMessageConnection
import com.krillsson.sysapi.logaccess.file.LogFileReader
import com.krillsson.sysapi.logaccess.file.LogFileService
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "LogFilesAccess")
class LogFileAccessResolver(
    private val logFilesManager: LogFilesManager,
    private val service: LogFileService
) {
    @SchemaMapping
    fun files(): List<LogFileReader> = logFilesManager.files()

    @SchemaMapping
    fun openLogFile(@Argument path: String): List<String> = logFilesManager.openLogFile(path)

    @SchemaMapping
    fun openLogFileConnection(
        @Argument path: String,
        @Argument after: String?,
        @Argument before: String?,
        @Argument first: Int?,
        @Argument last: Int?,
        @Argument reversed: Boolean?,
    ): LogMessageConnection {
        return service.getLogs(
            logFilePath = path,
            after = after,
            before = before,
            first = first,
            last = last,
            reverse = reversed
        )
    }
}

@Controller
@SchemaMapping(typeName = "LogFile")
class LogFileResolver {
    @SchemaMapping
    fun name(reader: LogFileReader) = reader.name()

    @SchemaMapping
    fun path(reader: LogFileReader) = reader.path()

    @SchemaMapping
    fun sizeBytes(reader: LogFileReader) = reader.sizeBytes()

    @SchemaMapping
    fun createdAt(reader: LogFileReader) = reader.createdAt()

    @SchemaMapping
    fun updatedAt(reader: LogFileReader) = reader.updatedAt()

    @SchemaMapping
    fun count(reader: LogFileReader) = reader.count()
}