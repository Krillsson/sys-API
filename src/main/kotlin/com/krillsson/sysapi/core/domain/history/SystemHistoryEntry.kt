package com.krillsson.sysapi.core.domain.history

import java.time.Instant
import java.util.*

class SystemHistoryEntry(val id: UUID, val date: Instant, val value: HistorySystemLoad)