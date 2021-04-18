package com.krillsson.sysapi.core.domain.event

import com.krillsson.sysapi.core.monitoring.MonitorType
import java.time.OffsetDateTime
import java.util.*

class OngoingEvent(
    id: UUID,
    monitorId: UUID,
    monitoredItemId: String?,
    monitorType: MonitorType,
    startTime: OffsetDateTime,
    threshold: Double,
    value: Double
) : Event(id, monitorId, monitoredItemId, monitorType, startTime, threshold, value)
