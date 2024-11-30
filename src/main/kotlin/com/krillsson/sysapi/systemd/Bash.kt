package com.krillsson.sysapi.systemd

import com.krillsson.sysapi.util.logger
import org.apache.commons.exec.*
import org.apache.commons.io.output.ByteArrayOutputStream
import reactor.core.publisher.Flux
import java.io.PipedInputStream
import java.io.PipedOutputStream
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

    fun executeToTextContinuously(command: String): Flux<String> {
        return Flux.create { emitter ->
            // Command to tail the service journal
            val commandLine = createBashCommand(command)

            // Setting up a pipe to stream logs
            val pipedOutputStream = PipedOutputStream()
            val pipedInputStream = PipedInputStream(pipedOutputStream)

            // Stream handler for real-time log capture
            val streamHandler = PumpStreamHandler(pipedOutputStream)

            // Executor to run the command
            val executor = DefaultExecutor().apply {
                setStreamHandler(streamHandler)
            }

            try {
                // Execute the command
                executor.execute(commandLine)

                // Read lines from the pipe and emit them
                pipedInputStream.bufferedReader().use { reader ->
                    reader.lines().forEach { line ->
                        emitter.next(line)
                    }
                }
            } catch (e: Exception) {
                emitter.error(e)
            }

            // Cleanup on disposal
            emitter.onDispose {
                try {
                    pipedInputStream.close()
                    pipedOutputStream.close()
                } catch (e: Exception) {
                    // Suppress cleanup exceptions
                }
            }
        }
    }

    private fun createBashCommand(command: String): CommandLine {
        val commandLine = CommandLine("/bin/sh")
        commandLine.addArguments("-c")
        commandLine.addArguments("'$command'", false)
        return commandLine
    }
}

