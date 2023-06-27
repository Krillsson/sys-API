package com.krillsson.sysapi.config

data class LinuxConfiguration(
    val systemDaemonServiceManagement: SystemDaemonServiceManagementConfiguration = SystemDaemonServiceManagementConfiguration(),
    val journalLogs: JournalLogsConfiguration = JournalLogsConfiguration()
)

data class SystemDaemonServiceManagementConfiguration(
    val enabled: Boolean = true
)

data class JournalLogsConfiguration(
    val enabled: Boolean = true
)