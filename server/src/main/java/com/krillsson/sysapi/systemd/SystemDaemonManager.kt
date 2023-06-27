package com.krillsson.sysapi.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.LinuxConfiguration
import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessAvailable
import com.krillsson.sysapi.util.logger


class SystemDaemonManager(
    private val mapper: ObjectMapper,
    private val config: LinuxConfiguration
) : SystemDaemonAccessAvailable {

    val logger by logger()

    private val journalCtl = JournalCtl(mapper, config.journalLogs)
    private val systemCtl = SystemCtl(mapper, config.systemDaemonServiceManagement)

    fun supportedBySystem(): Boolean {
        return journalCtl.supportedBySystem() && systemCtl.supportedBySystem()
    }

    override fun openJournal(name: String) =
        journalCtl.lines(name)

    override fun services(): List<SystemCtl.ListServicesOutput.Item> {
        return systemCtl.services()
    }

    override fun service(name: String): SystemCtl.ListServicesOutput.Item? {
        return systemCtl.service(name)
    }

    fun performCommandWithService(serviceName: String, command: SystemDaemonCommand): CommandResult {
        return systemCtl.performCommandWithService(serviceName, command)
    }
}
