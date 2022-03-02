package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.domain.system.SystemLoad

// To prevent repeating the logic for selecting a monitored value from a SystemLoad as it can cause bugs
typealias NumericalValueSelector = (SystemLoad, String?) -> MonitoredValue.NumericalValue?
typealias FractionalValueSelector = (SystemLoad, String?) -> MonitoredValue.FractionalValue?
typealias ConditionalValueSelector = (SystemLoad, String?) -> MonitoredValue.ConditionalValue?
typealias ContainerConditionalValueSelector = (List<Container>, String?) -> MonitoredValue.ConditionalValue?