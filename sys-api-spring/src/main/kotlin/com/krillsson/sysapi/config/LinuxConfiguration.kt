package com.krillsson.sysapi.config

data class LinuxConfiguration(
    var systemDaemonServiceManagement: SystemDaemonServiceManagementConfiguration = SystemDaemonServiceManagementConfiguration(),
    var journalLogs: JournalLogsConfiguration = JournalLogsConfiguration()
)

data class SystemDaemonServiceManagementConfiguration(
    var enabled: Boolean = true
)

data class JournalLogsConfiguration(
    var enabled: Boolean = true
)