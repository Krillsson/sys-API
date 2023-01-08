package com.krillsson.sysapi.core.domain.history

import java.time.OffsetDateTime
import java.util.*

open class HistoryEntry<T>(val id: UUID, val date: OffsetDateTime, val value: T)