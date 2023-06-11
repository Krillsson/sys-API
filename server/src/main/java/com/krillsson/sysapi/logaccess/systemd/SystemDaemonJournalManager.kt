package com.krillsson.sysapi.logaccess.systemd

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.graphql.domain.SystemDaemonJournalAccessAvailable


class SystemDaemonJournalManager(
    private val services: List<String>,
    private val mapper: ObjectMapper
) : SystemDaemonJournalAccessAvailable {

    fun supportedBySystem(): Boolean {
        return true
    }

    override fun openSystemDaemonJournal(name: String) = journals().firstOrNull { it.name() == name }?.lines().orEmpty()

    override fun journals(): List<SystemDaemonJournalReader> = services.map { SystemDaemonJournalReader(it, mapper) }

}