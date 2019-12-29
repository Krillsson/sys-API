package com.krillsson.sysapi.graphql.mutations


data class CreateMonitorInput(val inertiaInSeconds: Int,
                              val type: com.krillsson.sysapi.core.monitoring.MonitorType,
                              val threshold: Float)