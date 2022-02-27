package com.krillsson.sysapi.graphql.mutations

import com.krillsson.sysapi.core.monitoring.Monitor

data class CreateMonitorInput(val inertiaInSeconds: Int,
                              val type: Monitor.Type,
                              val threshold: Float,
                              val monitoredItemId: String?
)