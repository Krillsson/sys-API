package com.krillsson.sysapi.graphql.domain

import com.krillsson.sysapi.core.monitoring.Monitor
import java.time.OffsetDateTime
import java.util.*

class MonitorEvent(
        val id: UUID,
        val monitorId: UUID,
        val time: OffsetDateTime,
        val monitorType: Monitor.Type,
        val threshold: Double,
        val value: Double
)