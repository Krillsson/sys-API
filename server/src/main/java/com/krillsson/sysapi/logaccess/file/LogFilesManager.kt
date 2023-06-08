package com.krillsson.sysapi.logaccess.file

import com.krillsson.sysapi.config.LogReaderConfiguration
import com.krillsson.sysapi.util.logger
import java.io.File

class LogFilesManager(private val configuration: LogReaderConfiguration.Files) {

    val logger by logger()

    fun openLogFile(id: String): List<String> {
        val file = files().firstOrNull() { it.name() == id }
        return file?.lines().orEmpty()
    }

    fun files(): List<LogFileReader> {
        return configuration.paths.mapNotNull { path ->
            val file = File(path)
            when {
                file.exists() && file.isFile && file.canRead() -> LogFileReader(file)
                !file.exists() -> {
                    logger.error("$path does not exist")
                    null
                }

                file.isDirectory -> {
                    logger.error("$path is a directory")
                    null
                }

                !file.canRead() -> {
                    logger.error("$path does not exist")
                    null
                }

                else -> null
            }
        }
    }
}