package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.*
import com.krillsson.sysapi.graphql.mutations.BooleanValueMonitorType
import com.krillsson.sysapi.graphql.mutations.FractionalValueMonitorType
import com.krillsson.sysapi.graphql.mutations.NumericalValueMonitorType
import com.krillsson.sysapi.persistence.Store

class MonitorRepository(private val store: Store<List<MonitorStore.StoredMonitor>>) {

    fun read(): List<Monitor<MonitoredValue>> {
        return store.read()?.map { MonitorFactory.createMonitor(it.type, it.id, it.config.asConfig(it.type)) }.orEmpty()
    }

    fun write(content: List<Monitor<MonitoredValue>>) {
        store.write(content.map { it.asStoredMonitor() })
    }

    fun update(action: (List<Monitor<MonitoredValue>>?) -> List<Monitor<MonitoredValue>>) {
        val previousValue = read()
        val newValue = action(previousValue)
        write(newValue)
    }

    private fun Monitor<MonitoredValue>.asStoredMonitor(): MonitorStore.StoredMonitor {
        return MonitorStore.StoredMonitor(id, type, config.asStoredMonitorConfig())
    }

    private fun MonitorConfig<out MonitoredValue>.asStoredMonitorConfig(): MonitorStore.StoredMonitor.Config {
        return MonitorStore.StoredMonitor.Config(
            monitoredItemId, threshold.toDouble(), inertia
        )
    }

    private fun MonitorStore.StoredMonitor.Config.asConfig(type: Monitor.Type): MonitorConfig<MonitoredValue> {
        val convertedValue = when {
            BooleanValueMonitorType.values().any { it.name == type.name } -> threshold.toBooleanValue()
            FractionalValueMonitorType.values().any { it.name == type.name } -> threshold.toFractionalValue()
            NumericalValueMonitorType.values().any { it.name == type.name } -> threshold.toNumericalValue()
            else -> throw IllegalStateException("No equivalent to $this exists in ${Monitor.Type::class.simpleName}")
        }
        return MonitorConfig<MonitoredValue>(
            monitoredItemId, convertedValue, inertia
        )
    }

}