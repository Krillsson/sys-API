package com.krillsson.sysapi.graphql.mutations

import java.util.*

data class UpdateNumericalMonitorInput(
        val monitorId: UUID,
        val inertiaInSeconds: Int?,
        val threshold: Long?,
)

data class UpdateFractionMonitorInput(
        val monitorId: UUID,
        val inertiaInSeconds: Int?,
        val threshold: Float?,
)

data class UpdateConditionalMonitorInput(
        val monitorId: UUID,
        val inertiaInSeconds: Int?,
        val threshold: Boolean?,
)

interface UpdateMonitorOutput

data class UpdateMonitorOutputSucceeded(
        val monitorId: UUID,
) : UpdateMonitorOutput

data class UpdateMonitorOutputFailed(
        val reason: String
) : UpdateMonitorOutput