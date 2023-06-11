package com.krillsson.sysapi.util

import java.io.BufferedReader
import java.io.File
import java.util.concurrent.TimeUnit

object ExecuteCommand {

    val logger by logger()
    fun asBufferedReader(
        command: String,
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ): BufferedReader? = runCatching {
        ProcessBuilder("\\s".toRegex().split(command))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
            .inputStream.bufferedReader()
    }.onFailure {
        logger.error("Error while executing $this", it)
    }.getOrNull()

    fun asString(
        command: String,
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ) = asBufferedReader(
        command, workingDir, timeoutAmount, timeoutUnit
    )?.readText()
}