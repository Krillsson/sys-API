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
        startProcess(command, workingDir, timeoutAmount, timeoutUnit)
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

    fun asExitStatus(
        command: String,
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ): Result<Int> = kotlin.runCatching {
        startProcess(
            command, workingDir, timeoutAmount, timeoutUnit
        ).exitValue()
    }

    private fun startProcess(
        command: String,
        workingDir: File,
        timeoutAmount: Long,
        timeoutUnit: TimeUnit
    ): Process {
        logger.info("Executing $command")
        return ProcessBuilder("\\s".toRegex().split(command))
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
    }

    fun checkIfCommandExistsUsingBash(
        command: String,
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
    ): Result<Boolean> = kotlin.runCatching {
        logger.info("Executing sh -c \"command -v $command &> /dev/null\"")
        val process = ProcessBuilder("sh", "-c", "command -v $command &> /dev/null")
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitFor(timeoutAmount, timeoutUnit)
        val result = process.exitValue() == 0
        result
    }
}