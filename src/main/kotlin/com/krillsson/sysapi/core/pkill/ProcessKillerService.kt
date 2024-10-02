package com.krillsson.sysapi.core.pkill

import com.krillsson.sysapi.util.logger
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class ProcessKillerService {

    enum class ProcessKillResult {
        RequestSuccess,
        RequestDenied,
        NotAllowed,
        UnableToKillOwnProcess,
        ProcessNotFound,
        UnknownError,
    }

    val logger by logger()

    fun isAlive(pid: Long): Boolean {
        val process = pid.asProcess()
        return process?.isAlive ?: false
    }

    fun kill(pid: Long, forcibly: Boolean): ProcessKillResult {
        return try {
            logger.info("Attempting to kill process ${if (forcibly) "forcibly" else "gracefully"} (pid: $pid)")
            val process = pid.asProcess()
            if (process != null) {
                process.onExit().thenRun {
                    logger.info("Process terminated: (pid: $pid)")
                }
                val result = if (forcibly) process.destroyForcibly() else process.destroy()
                if (result) {
                    logger.info("Process kill ${if (forcibly) "forcibly" else "gracefully"} request success (pid: $pid)")
                    ProcessKillResult.RequestSuccess
                } else {
                    logger.warn("Process kill ${if (forcibly) "forcibly" else "gracefully"} request denied (pid: $pid)")
                    ProcessKillResult.RequestDenied
                }
            } else {
                ProcessKillResult.ProcessNotFound
            }
        } catch (exception: IllegalStateException) {
            logger.error("Not allowed to kill our own process ${if (forcibly) "forcibly" else "gracefully"} (pid: $pid)", exception)
            ProcessKillResult.UnableToKillOwnProcess
        } catch (exception: SecurityException) {
            logger.error("Not allowed to kill process ${if (forcibly) "forcibly" else "gracefully"} due to security policy (pid: $pid)", exception)
            ProcessKillResult.NotAllowed
        } catch (throwable: Throwable) {
            logger.error("Unknown error occurred killing process ${if (forcibly) "forcibly" else "gracefully"} (pid: $pid)", throwable)
            ProcessKillResult.UnknownError
        }
    }

    private fun Long.asProcess(): ProcessHandle? {
        val process = ProcessHandle.of(this).getOrNull()
        if (process != null) {
            logger.info("Successfully got handle for ${process.info().command().getOrElse { "unknown" }} (pid: $this)")
        } else {
            logger.info("Unable to get handle for pid: $this")
        }
        return process
    }
}