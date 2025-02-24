package com.krillsson.sysapi.config

data class LinuxConfiguration(
    val systemDaemonServiceManagement: SystemDaemonServiceManagementConfiguration = SystemDaemonServiceManagementConfiguration(),
    val journalLogs: JournalLogsConfiguration = JournalLogsConfiguration(),
    val overrideCpuTempSensor: String? = null
)

data class SystemDaemonServiceManagementConfiguration(
    val enabled: Boolean = true
)

data class JournalLogsConfiguration(
    val enabled: Boolean = true
)