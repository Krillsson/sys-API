package com.krillsson.sysapi.core.domain.history

import com.krillsson.sysapi.core.domain.history.HistoryEntry
import com.krillsson.sysapi.core.domain.system.SystemLoad
import java.time.OffsetDateTime

class SystemHistoryEntry(date: OffsetDateTime, value: SystemLoad) : HistoryEntry<SystemLoad>(date, value)