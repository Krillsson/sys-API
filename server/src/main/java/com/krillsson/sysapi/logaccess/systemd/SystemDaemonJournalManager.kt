package com.krillsson.sysapi.logaccess.systemd

import com.krillsson.sysapi.graphql.domain.SystemDaemonJournalAccessAvailable
import org.metabit.platform.interfacing.jjournal.Journal


class SystemDaemonJournalManager(private val services: List<String> = listOf("ssh.service", "fail2ban.service")) :
    SystemDaemonJournalAccessAvailable {

    fun supportedBySystem(): Boolean {
        return Journal().hasRuntimeFiles()
    }

    override fun openSystemDaemonJournal(name: String) = journals().firstOrNull()?.lines().orEmpty()

    override fun journals(): List<SystemDaemonJournalReader> = services.map { SystemDaemonJournalReader(it) }

}