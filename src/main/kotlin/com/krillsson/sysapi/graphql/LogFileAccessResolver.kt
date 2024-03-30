package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.logaccess.file.LogFileReader
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "LogFilesAccess")
class LogFileAccessResolver(private val logFilesManager: LogFilesManager) {
    @SchemaMapping
    fun files(): List<LogFileReader> = logFilesManager.files()

    @SchemaMapping
    fun openLogFile(@Argument path: String): List<String> = logFilesManager.openLogFile(path)
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