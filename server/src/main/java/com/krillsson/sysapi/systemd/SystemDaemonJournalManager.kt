package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessAvailable
import com.krillsson.sysapi.util.ExecuteCommand
import com.krillsson.sysapi.util.logger


class SystemDaemonJournalManager(
    private val mapper: ObjectMapper
) : SystemDaemonAccessAvailable {

    val logger by logger()

    fun supportedBySystem(): Boolean {
        return ExecuteCommand.checkIfCommandExistsUsingBash("journalctl").getOrNull() ?: false
    }

    sealed class CommandResult {
        object Success : CommandResult()
        object Unavailable : CommandResult()
        data class Failed(val error: Throwable) : CommandResult()
    }

    private fun queryJournalCtl(): List<Services.ServicesItem> {
        val services = mutableListOf<Services.ServicesItem>()
        ExecuteCommand.asBufferedReader("systemctl --output=json --type=service")?.use { reader ->
            val data = mapper.readValue(reader, Services::class.java)
            data.forEach {
                services.add(it)
            }
        }
        return services
    }

    override fun openJournal(name: String) =
        createUnit(name)?.lines()
            .orEmpty()

    private fun createUnit(name: String): SystemDaemonUnit? {
        return services().firstOrNull { it.unit == name }?.let { SystemDaemonUnit(it.unit, mapper) }
    }

    override fun services(): List<Services.ServicesItem> = queryJournalCtl()
    fun performCommandWithService(serviceName: String, command: SystemDaemonCommand): CommandResult {
        return if (!supportedBySystem()) {
            CommandResult.Unavailable
        } else {
            val unit = createUnit(serviceName)
            if (unit != null) {
                unit.performCommand(command)
            } else {

                CommandResult.Failed(java.lang.RuntimeException())
            }
        }
    }

}