package com.krillsson.sysapi.core.domain.history

import java.time.OffsetDateTime

open class HistoryEntry<T>(val date: OffsetDateTime, val value: T)