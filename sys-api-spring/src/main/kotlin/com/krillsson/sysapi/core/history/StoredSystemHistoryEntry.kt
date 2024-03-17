package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.history.HistorySystemLoad
import java.time.OffsetDateTime

class StoredSystemHistoryEntry(val date: OffsetDateTime, val value: HistorySystemLoad)