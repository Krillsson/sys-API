package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.SystemDaemonServiceManagementConfiguration
import com.krillsson.sysapi.util.Bash
import javax.ws.rs.NotFoundException

class SystemCtl(
    private val mapper: ObjectMapper,
    private val config: SystemDaemonServiceManagementConfiguration
) {

    companion object {
        private const val LIST_SERVICES_COMMAND = "systemctl --output=json --type=service"
    }

    class ListServicesOutput : ArrayList<ListServicesOutput.Item>() {
        data class Item(
            val active: String,
            val description: String,
            val load: String,
            val sub: String,
            val unit: String
        )
    }

    fun supportedBySystem(): Boolean {
        return Bash.checkIfCommandExists("systemctl").getOrNull() ?: false
    }

    fun services(): List<ListServicesOutput.Item> {
        val services = mutableListOf<ListServicesOutput.Item>()
        val result = Bash.executeToText(LIST_SERVICES_COMMAND)
        result.map { json ->
            val data = mapper.readValue(json, ListServicesOutput::class.java)
            data.forEach {
                services.add(it)
            }
        }
        return services
    }

    fun service(name: String): ListServicesOutput.Item? {
        return services().firstOrNull { it.unit == name }
    }

    fun performCommandWithService(serviceName: String, command: SystemDaemonCommand): CommandResult {
        return when {
            !supportedBySystem() -> CommandResult.Unavailable
            !config.enabled -> CommandResult.Disabled
            !hasUnit(serviceName) -> CommandResult.Failed(NotFoundException("$serviceName does not exist"))
            else -> {
                performCommand(serviceName, command)
            }
        }
    }

    private fun performCommand(serviceUnitName: String, command: SystemDaemonCommand): CommandResult {
        val exitStatus = Bash.executeToExitStatus("systemctl ${command.name.lowercase()} $serviceUnitName")
        return exitStatus.fold(
            onSuccess = {
                if (it == 0) {
                    CommandResult.Success
                } else {
                    CommandResult.Failed(RuntimeException("Failed with $it"))
                }
            },
            onFailure = {
                CommandResult.Failed(it)
            }
        )
    }

    private fun hasUnit(name: String): Boolean {
        return services().any { it.unit == name }
    }
}