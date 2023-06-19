package com.krillsson.sysapi.config

data class LinuxConfiguration(
    val systemDaemon: SystemDaemonConfiguration = SystemDaemonConfiguration()
)

data class SystemDaemonConfiguration(
    val enabled: Boolean = true
)