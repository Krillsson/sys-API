package com.krillsson.sysapi.graphql.mutations

import java.util.*

data class UpdateMonitorInput(
    val monitorId: UUID,
    val inertiaInSeconds: Int?,
    val threshold: Float?,
)

interface UpdateMonitorOutput

data class UpdateMonitorOutputSucceeded(
    val monitorId: UUID,
) : UpdateMonitorOutput

data class UpdateMonitorOutputFailed(
    val reason: String
) : UpdateMonitorOutput