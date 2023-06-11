package com.krillsson.sysapi.logaccess

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.LogReaderConfiguration
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import com.krillsson.sysapi.logaccess.systemd.SystemDaemonJournalManager
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsEventLogManager
import com.krillsson.sysapi.util.logger

class LogAccessManager(
    private val configuration: LogReaderConfiguration,
    private val mapper: ObjectMapper
) {

    val logger by logger()

    val systemDaemonJournalManager = SystemDaemonJournalManager(configuration.journal.services, mapper)
    val logFilesManager = LogFilesManager(configuration = configuration.files)
    val windowsEventLogManager = WindowsEventLogManager()
}