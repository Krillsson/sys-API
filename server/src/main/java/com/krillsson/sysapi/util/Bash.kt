package com.krillsson.sysapi.util

import org.apache.commons.exec.*
import org.apache.commons.io.output.ByteArrayOutputStream
import java.nio.charset.Charset


object Bash {

    val logger by logger()

    fun executeToText(
        command: String,
        timeoutAmountMillis: Long = 3 * 1000
    ): Result<String> {
        val commandLine = createBashCommand(command)
        return try {
            val watchdog = ExecuteWatchdog(timeoutAmountMillis)
            val resultHandler = DefaultExecuteResultHandler()
            val stdout = ByteArrayOutputStream()
            val psh = PumpStreamHandler(stdout)
            logger.debug("Executing ${commandLine.toStrings().joinToString(" ")}")
            val exec = DefaultExecutor()
            exec.streamHandler = psh
            exec.watchdog = watchdog
            exec.execute(commandLine, resultHandler)
            resultHandler.waitFor()
            Result.success(stdout.toString(Charset.defaultCharset()))
        } catch (throwable: Throwable) {
            logger.error("Failed to execute ${commandLine.toStrings().joinToString(" ")}", throwable)
            Result.failure(throwable)
        }
    }

    fun executeToExitStatus(
        command: String,
        timeoutAmountMillis: Long = 3 * 1000
    ): Result<Int> {
        val commandLine = createBashCommand(command)
        return try {
            val resultHandler = executeWithWatchdog(timeoutAmountMillis, commandLine)
            Result.success(resultHandler.exitValue)
        } catch (throwable: Throwable) {
            logger.error("Failed to execute ${commandLine.toStrings().joinToString(" ")}", throwable)
            Result.failure(throwable)
        }
    }

    fun checkIfCommandExists(
        command: String,
        timeoutAmountMillis: Long = 3 * 1000
    ): Result<Boolean> {
        val commandLine = CommandLine("/bin/sh")
        commandLine.addArguments("-c")
        commandLine.addArguments("'command -v $command &> /dev/null'", false)
        return try {
            val resultHandler = executeWithWatchdog(timeoutAmountMillis, commandLine)
            Result.success(resultHandler.exitValue == 0)
        } catch (throwable: Throwable) {
            logger.error("Failed to execute ${commandLine.toStrings().joinToString(" ")}", throwable)
            Result.failure(throwable)
        }
    }

    private fun executeWithWatchdog(
        timeoutAmountMillis: Long,
        commandLine: CommandLine
    ): DefaultExecuteResultHandler {
        val watchdog = ExecuteWatchdog(timeoutAmountMillis)
        val resultHandler = DefaultExecuteResultHandler()
        logger.debug("Executing ${commandLine.toStrings().joinToString(" ")}")
        val exec = DefaultExecutor()
        exec.watchdog = watchdog
        exec.execute(commandLine, resultHandler)
        resultHandler.waitFor()
        return resultHandler
    }

    private fun createBashCommand(command: String): CommandLine {
        val commandLine = CommandLine("/bin/sh")
        commandLine.addArguments("-c")
        commandLine.addArguments("'$command'", false)
        return commandLine
    }
}

