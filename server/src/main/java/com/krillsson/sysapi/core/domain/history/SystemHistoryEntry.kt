package com.krillsson.sysapi.core.domain.history

import java.time.OffsetDateTime
import java.util.*

class SystemHistoryEntry(val id: UUID, val date: OffsetDateTime, val value: HistorySystemLoad)