package com.krillsson.sysapi.graphql.mutations

import com.krillsson.sysapi.core.monitoring.MonitorType

data class CreateMonitorInput(val inertiaInSeconds: Int,
                              val type: MonitorType,
                              val threshold: Float,
                              val monitoredItemId: String?
)