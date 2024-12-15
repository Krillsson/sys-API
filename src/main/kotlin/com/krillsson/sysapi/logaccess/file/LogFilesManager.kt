package com.krillsson.sysapi.logaccess.file

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.util.logger
import org.springframework.stereotype.Service
import java.io.File

@Service
class LogFilesManager(private val configuration: YAMLConfigFile) {

    val logger by logger()

    companion object {
        private val VALID_EXTENSIONS = listOf(
            "log",
            "txt",
            ""
        )
    }

    fun openLogFile(path: String): List<String> {
        val file = files().firstOrNull() { it.path() == path }
        return file?.lines().orEmpty()
    }

    fun files(): List<LogFileReader> {
        val list = mutableListOf<LogFileReader>()
        configuration.logReader.directories.orEmpty().map { path ->
            val directory = File(path)
            if (directory.isDirectory) {
                directory.listFiles { file -> file.isFile && VALID_EXTENSIONS.contains(file.extension) }
                    .forEach { file ->
                        if (file.canRead()) {
                            list.add(LogFileReader(file))
                        }
                    }
            }
        }

        configuration.logReader.files.orEmpty().mapNotNull { path ->
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
        }.forEach {
            list += it
        }
        return list
    }
}