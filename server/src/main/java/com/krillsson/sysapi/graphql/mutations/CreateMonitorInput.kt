package com.krillsson.sysapi.graphql.mutations

import com.krillsson.sysapi.core.monitoring.Monitor

data class CreateMonitorInput(val inertiaInSeconds: Int,
                              val type: Monitor.Type,
                              val threshold: Float,
                              val monitoredItemId: String?
)

data class CreateNumericalMonitorInput (
    val inertiaInSeconds: Int,
    val type: NumericalValueMonitorType,
    val threshold: Long,
    val monitoredItemId: String?
)

data class CreateFractionMonitorInput (
    val inertiaInSeconds: Int,
    val type: FractionalValueMonitorType,
    val threshold: Float,
    val monitoredItemId: String?
)

data class CreateBooleanMonitorInput (
    val inertiaInSeconds: Int,
    val type: BooleanValueMonitorType,
    val threshold: Boolean,
    val monitoredItemId: String?
)