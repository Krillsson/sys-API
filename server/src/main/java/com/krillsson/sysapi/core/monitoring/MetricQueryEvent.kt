package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.system.SystemLoad

class MetricQueryEvent(val load: SystemLoad, val containers: List<Container>)