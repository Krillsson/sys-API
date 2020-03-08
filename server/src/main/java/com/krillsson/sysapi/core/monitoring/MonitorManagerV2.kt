package com.krillsson.sysapi.core.monitoring

import com.google.common.eventbus.EventBus
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.dto.monitor.Monitor
import com.krillsson.sysapi.persistence.Store
import io.dropwizard.lifecycle.Managed
import java.util.*

class MonitorManagerV2(private val eventBus: EventBus, private val store: Store<HashMap<String, Monitor>>, private val provider: Metrics) : Managed {
    private val activeMonitorInputs: MutableMap<String, MonitorInput> = HashMap()
    private val activeMonitors: MutableMap<String, Monitor> = HashMap()


    override fun start() {

    }

    override fun stop() {

    }
}