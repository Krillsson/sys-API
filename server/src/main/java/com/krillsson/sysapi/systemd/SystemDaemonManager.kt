package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.SystemDaemonConfiguration
import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessAvailable
import com.krillsson.sysapi.util.ExecuteCommand
import com.krillsson.sysapi.util.logger


class SystemDaemonManager(
    private val mapper: ObjectMapper,
    private val config: SystemDaemonConfiguration
) : SystemDaemonAccessAvailable {

    val logger by logger()

    companion object {
        private const val LIST_SERVICES_COMMAND = "systemctl --output=json --type=service"
    }

    fun supportedBySystem(): Boolean {
        return ExecuteCommand.checkIfCommandExistsUsingBash("journalctl").getOrNull() ?: false
    }

    override fun openJournal(name: String) =
        createUnit(name)?.lines()
            .orEmpty()

    override fun services(): List<SystemCtlServicesOutput.Item> {
        val services = mutableListOf<SystemCtlServicesOutput.Item>()
        ExecuteCommand.asBufferedReader(LIST_SERVICES_COMMAND)?.use { reader ->
            val data = mapper.readValue(reader, SystemCtlServicesOutput::class.java)
            data.forEach {
                services.add(it)
            }
        }
        return services
    }

    fun service(name: String): SystemCtlServicesOutput.Item? {
        return services().firstOrNull { it.unit == name }
    }

    fun performCommandWithService(serviceName: String, command: SystemDaemonCommand): CommandResult {
        return if (!supportedBySystem()) {
            CommandResult.Unavailable
        } else {
            val unit = createUnit(serviceName)
            when {
                unit != null -> {
                    unit.performCommand(command)
                }

                else -> {
                    CommandResult.Failed(java.lang.RuntimeException())
                }
            }
        }
    }

    private fun createUnit(name: String): SystemDaemonUnit? {
        return services().firstOrNull { it.unit == name }?.let { SystemDaemonUnit(it.unit, mapper) }
    }
}
