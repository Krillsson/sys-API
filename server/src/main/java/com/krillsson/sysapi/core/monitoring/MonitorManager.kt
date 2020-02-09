package com.krillsson.sysapi.core.monitoring

import com.fasterxml.jackson.core.type.TypeReference
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.monitors.*
import com.krillsson.sysapi.persistence.JsonFile
import io.dropwizard.lifecycle.Managed
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.stream.Collectors

class MonitorManager(private val eventBus: EventBus, private val persistentMonitors: JsonFile<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>>, private val provider: Metrics) : Managed {
    private val activeMonitors: MutableMap<String, Monitor?> = HashMap()
    private val events: MutableList<MonitorEvent> = ArrayList()

    fun addMonitor(monitor: MonitorInput): String {
        if (monitor.id == null) {
            monitor.setId(UUID.randomUUID().toString())
        }
        if (activeMonitors.containsKey(monitor.id())) {
            LOGGER.debug("Updating monitoring")
        }
        activeMonitors[monitor.id()] = monitor
        persist()
        return monitor.id()
    }

    @Subscribe
    fun onEvent(event: MonitorMetricQueryEvent) {
        for (activeMonitor in activeMonitors.values) {
            val check = activeMonitor!!.check(event.load())
            check.ifPresent { e: MonitorEvent ->
                LOGGER.warn("Event: {}", e)
                if (e.monitorStatus == MonitorEvent.MonitorStatus.STOP && events.stream()
                                .noneMatch { me: MonitorEvent -> me.id == e.id }) {
                    LOGGER.warn("Received STOP event for explicitly removed event, ignoring...")
                } else {
                    events.add(e)
                }
            }
        }
    }

    fun validate(monitor: Monitor): Boolean {
        return monitor.value(provider.consolidatedMetrics()) != -1.0
    }

    fun createAndAdd(inertiaInSeconds: Int, type: MonitorType, threshold: Double): String? {
        val monitor = create(type, threshold, Duration.ofSeconds(inertiaInSeconds.toLong()))
        if (validate(monitor)) {
            addMonitor(monitor)
            return monitor.id()
        }
        return null
    }

    fun monitors(): List<Monitor?> {
        return activeMonitors.values.toList()
    }

    fun events(): List<MonitorEvent> {
        return Collections.unmodifiableList(events)
    }

    @Throws(Exception::class)
    override fun start() {
        persistentMonitors.getPersistedData<Any>(false, JsonFile.Result<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>, Any> { value: HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor> ->
            value.entries.stream().forEach { e: Map.Entry<String, com.krillsson.sysapi.dto.monitor.Monitor> ->
                LOGGER.debug("Registering monitoring ID: {} threshold: {}", e.value, e.value.threshold)
                activeMonitors[e.key] = MonitorMapper.INSTANCE.map(e.value)
            }
            null
        })
        eventBus.register(this)
    }

    @Throws(Exception::class)
    override fun stop() {
        persist()
        eventBus.unregister(this)
    }

    private fun persist() {


        persistentMonitors.getPersistedData<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>>(true,
                JsonFile.Result<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>, HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>> { persistedMap: HashMap<String?, com.krillsson.sysapi.dto.monitor.Monitor?> ->
                    activeMonitors.forEach { (key: String?, value: Monitor?) -> persistedMap[key] = MonitorMapper.INSTANCE.map(value) }
                    persistedMap
                })
    }

    fun removeEvents(id: String?): String? {
        val toBeRemoved: MutableList<MonitorEvent> = ArrayList()
        for (event in events) {
            if (event.id.toString().equals(id, ignoreCase = true)) {
                toBeRemoved.add(event)
            }
        }
        LOGGER.debug("Removed {} events with ID {}", toBeRemoved.size, id)
        val status = events.removeAll(toBeRemoved)
        return if (status) {
            id
        } else {
            null
        }
    }

    fun remove(id: String?): String? {
        val status = activeMonitors.remove(id) != null
        return if (status) {
            persist()
            id
        } else {
            null
        }
    }

    fun monitorById(id: String?): Optional<Monitor> {
        return Optional.ofNullable(activeMonitors[id])
    }

    fun eventsForMonitorWithId(id: String?): Optional<List<MonitorEvent>> {
        return if (activeMonitors[id] != null) {
            Optional.of(events.stream()
                    .filter { event: MonitorEvent -> event.monitorId.equals(id, ignoreCase = true) }
                    .collect(Collectors.toList()))
        } else {
            Optional.empty()
        }
    }

    fun create(type: MonitorType?, threshold: Double, durationSeconds: Duration?): Monitor {
        val id = UUID.randomUUID().toString()
        return when (type) {
            MonitorType.CPU_LOAD -> CpuMonitor(
                    id,
                    durationSeconds,
                    threshold
            )
            MonitorType.CPU_TEMP -> CpuTemperatureMonitor(
                    id,
                    durationSeconds,
                    threshold
            )
            MonitorType.DRIVE_SPACE -> DriveMonitor(
                    id,
                    durationSeconds,
                    threshold
            )
            MonitorType.GPU_LOAD -> GpuMonitor(
                    id,
                    durationSeconds,
                    threshold
            )
            MonitorType.MEMORY_SPACE -> MemoryMonitor(
                    id,
                    durationSeconds,
                    threshold
            )
            MonitorType.NETWORK_UP -> NetworkUpMonitor(
                    id,
                    durationSeconds,
                    threshold
            )
            else -> throw IllegalArgumentException("Not supported")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MonitorManager::class.java)
        fun monitorsTypeReference(): TypeReference<HashMap<String, com.krillsson.sysapi.dto.monitor.Monitor>> {
            return object : TypeReference<HashMap<String?, com.krillsson.sysapi.dto.monitor.Monitor?>?>() {}
        }

        fun eventsTypeReference(): TypeReference<List<com.krillsson.sysapi.dto.monitor.MonitorEvent>> {
            return object : TypeReference<List<com.krillsson.sysapi.dto.monitor.MonitorEvent?>?>() {}
        }
    }

}