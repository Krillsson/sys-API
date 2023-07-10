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

    private val supportedBySystem = journalCtl.supportedBySystem() && systemCtl.supportedBySystem()

    fun supportedBySystem(): Boolean {
        return supportedBySystem
    }

    override fun openJournal(name: String, limit: Int) =
        journalCtl.lines(name, limit)

    override fun services(): List<SystemCtl.ListServicesOutput.Item> {
        return systemCtl.services()
    }

    override fun serviceDetails(name: String): SystemCtl.ServiceDetailsOutput? {
        return systemCtl.serviceDetails(name)
    }

    fun performCommandWithService(serviceName: String, command: SystemDaemonCommand): CommandResult {
        return systemCtl.performCommandWithService(serviceName, command)
    }
}
