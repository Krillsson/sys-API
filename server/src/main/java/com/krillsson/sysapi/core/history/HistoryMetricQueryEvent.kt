package com.krillsson.sysapi.core.history

import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.query.MetricQueryEvent

class HistoryMetricQueryEvent(load: SystemLoad) : MetricQueryEvent(load)