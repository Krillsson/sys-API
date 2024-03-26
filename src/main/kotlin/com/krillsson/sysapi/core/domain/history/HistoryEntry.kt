package com.krillsson.sysapi.core.domain.history

import java.time.Instant
import java.util.*

open class HistoryEntry<T>(val id: UUID, val date: Instant, val value: T)