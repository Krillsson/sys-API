package com.krillsson.sysapi.logaccess.systemd


class SystemDaemonJournalManager(private val services: List<String> = listOf("ssh.service", "fail2ban.service")) {
    fun services(): List<SystemDaemonJournalReader> = services.map { SystemDaemonJournalReader(it) }
}