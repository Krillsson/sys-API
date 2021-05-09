package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.query.MetricQueryEvent

class MonitorMetricQueryEvent(val load: SystemLoad, val containers: List<Container>) : MetricQueryEvent(load)