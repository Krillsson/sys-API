package com.krillsson.sysapi.logaccess

import com.krillsson.sysapi.config.LogReaderConfiguration
import com.krillsson.sysapi.logaccess.systemd.SystemDaemonJournalManager
import com.krillsson.sysapi.util.logger

class LogAccessManager(private val configuration: LogReaderConfiguration) {

    val logger by logger()

    val systemDaemonJournalManager = SystemDaemonJournalManager()

    fun systemdJournals() = systemDaemonJournalManager.services()

    fun openSystemDJournal(id: String) =
        systemDaemonJournalManager.services().firstOrNull { it.name() == id }?.lines().orEmpty()
}