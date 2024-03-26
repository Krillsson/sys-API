package com.krillsson.sysapi.graphql.mutations

import com.krillsson.sysapi.systemd.SystemDaemonCommand

interface PerformSystemDaemonCommandOutput

data class PerformSystemDaemonCommandOutputSucceeded(
    val serviceName: String
) : PerformSystemDaemonCommandOutput

data class PerformSystemDaemonCommandOutputFailed(
    val reason: String
) : PerformSystemDaemonCommandOutput

data class PerformSystemDaemonCommandInput(
    val serviceName: String,
    val command: SystemDaemonCommand
)


