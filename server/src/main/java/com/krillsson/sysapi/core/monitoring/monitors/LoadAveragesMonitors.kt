package com.krillsson.sysapi.core.monitoring.monitors

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.monitoring.Monitor
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import java.util.*

class LoadAverageMonitorOneMinute(override val id: UUID, override val config: MonitorConfig<MonitoredValue.FractionalValue>) :
    Monitor<MonitoredValue.FractionalValue>() {

    companion object {
        val selector: FractionalValueSelector = { load, _ ->
            MonitoredValue.FractionalValue(load.cpuLoad.loadAverages.oneMinute.toFloat())
        }
    }

    override val type: Type = Type.LOAD_AVERAGE_ONE_MINUTE

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.FractionalValue? =
        selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.FractionalValue): Boolean {
        return value > config.threshold
    }
}

class LoadAverageMonitorFiveMinutes(override val id: UUID, override val config: MonitorConfig<MonitoredValue.FractionalValue>) :
    Monitor<MonitoredValue.FractionalValue>() {

    companion object {
        val selector: FractionalValueSelector = { load, _ ->
            MonitoredValue.FractionalValue(load.cpuLoad.loadAverages.fiveMinutes.toFloat())
        }
    }

    override val type: Type = Type.LOAD_AVERAGE_FIVE_MINUTES

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.FractionalValue? =
        selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.FractionalValue): Boolean {
        return value > config.threshold
    }
}

class LoadAverageMonitorFifteenMinutes(override val id: UUID, override val config: MonitorConfig<MonitoredValue.FractionalValue>) :
    Monitor<MonitoredValue.FractionalValue>() {

    companion object {
        val selector: FractionalValueSelector = { load, _ ->
            MonitoredValue.FractionalValue(load.cpuLoad.loadAverages.fifteenMinutes.toFloat())
        }
    }

    override val type: Type = Type.LOAD_AVERAGE_FIFTEEN_MINUTES

    override fun selectValue(event: MonitorMetricQueryEvent): MonitoredValue.FractionalValue? =
        selector(event.load, null)

    override fun isPastThreshold(value: MonitoredValue.FractionalValue): Boolean {
        return value > config.threshold
    }
}