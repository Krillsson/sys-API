package com.krillsson.sysapi.core.domain.event

import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.OffsetDateTime
import java.util.UUID

class MonitorEvent(
    val id: UUID,
    val monitorId: UUID,
    val time: OffsetDateTime,
    val eventType: EventType,
    val monitorType: MonitorType,
    val threshold: Double,
    val value: Double
)