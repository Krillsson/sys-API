package com.krillsson.sysapi.core.domain.event

import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.OffsetDateTime
import java.util.*

class PastEvent(
    id: UUID,
    monitorId: UUID,
    monitoredItemId: String?,
    startTime: OffsetDateTime,
    val endTime: OffsetDateTime,
    monitorType: MonitorType,
    threshold: Double,
    value: Double
) : Event(id, monitorId, monitoredItemId, monitorType, startTime, threshold, value)