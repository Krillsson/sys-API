package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.persistence.Store

class MonitorRepository(private val store: Store<List<MonitorStore.StoredMonitor>>) {

    fun read(): List<Monitor> {
        return store.read()?.map { MonitorFactory.createMonitor(it.type, it.id, it.config.asConfig()) }.orEmpty()
    }

    fun write(content: List<Monitor>) {
        store.write(content.map { it.asStoredMonitor() })
    }

    fun update(action: (List<Monitor>?) -> List<Monitor>) {
        val previousValue = read()
        val newValue = action(previousValue)
        write(newValue)
    }

    private fun Monitor.asStoredMonitor(): MonitorStore.StoredMonitor {
        return MonitorStore.StoredMonitor(id, type, config.asStoredMonitorConfig())
    }

    private fun MonitorConfig.asStoredMonitorConfig(): MonitorStore.StoredMonitor.Config {
        return MonitorStore.StoredMonitor.Config(
            monitoredItemId, threshold, inertia
        )
    }

    private fun MonitorStore.StoredMonitor.Config.asConfig(): MonitorConfig {
        return MonitorConfig(
            monitoredItemId, threshold, inertia
        )
    }

}