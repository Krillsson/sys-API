package com.krillsson.sysapi.core.monitoring

import com.krillsson.sysapi.core.domain.monitor.MonitorConfig
import com.krillsson.sysapi.core.domain.monitor.MonitoredValue
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorFactory.createMonitor
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskInterval
import com.krillsson.sysapi.periodictasks.TaskManager
import com.krillsson.sysapi.util.Clock
import com.krillsson.sysapi.util.logger
import io.dropwizard.lifecycle.Managed
import java.time.Duration
import java.util.*

class MonitorManager(
    private val taskManager: TaskManager,
    private val metrics: Metrics,
    private val containerManager: ContainerManager,
    private val eventManager: EventManager,
    private val repository: MonitorRepository,
    private val monitoredItemMissingChecker: MonitoredItemMissingChecker,
    private val clock: Clock
) : Managed, Task {

    val logger by logger()

    override val defaultInterval: TaskInterval = TaskInterval.LessOften
    override val key: Task.Key = Task.Key.CheckMonitors

    override fun run() {
        val containerIds = idsSubset(containerTypes)
        val containerStatisticsIds = idsSubset(containerStatisticsTypes)
        val containers = if (containerIds.isNotEmpty()) containerManager.containersWithIds(containerIds) else emptyList()
        val containersStats = containerStatisticsIds.mapNotNull { id -> containerManager.statsForContainer(id) }
        checkMonitors(
            MonitorInput(
                metrics.systemMetrics().systemLoad(),
                containers,
                containersStats
            )
        )
    }

    companion object {
        val logger by logger()
    }

    private lateinit var activeMonitors: MutableMap<UUID, Pair<MonitorMechanism, Monitor<MonitoredValue>>>

    private fun checkMonitors(metricQueryEvent: MonitorInput) {
        activeMonitors.values.forEach { (mechanism, monitor) ->
            val value = monitor.selectValue(metricQueryEvent)
            if (value != null) {
                val isOverThreshold = monitor.isPastThreshold(value)
                val event = mechanism.check(
                    monitor,
                    monitor.config,
                    value,
                    isOverThreshold
                )
                event?.let {
                    eventManager.add(it)
                }
            }
            reportItemMissingForMonitor(monitor, value)
        }
    }

    override fun start() {
        restore()
        taskManager.registerTask(this)
    }


    override fun stop() {
        persist()
    }

    fun getAll() = activeMonitors.map { it.value.second }

    fun getById(id: UUID): Monitor<MonitoredValue>? {
        return activeMonitors[id]?.second
    }

    fun add(inertia: Duration, type: Monitor.Type, threshold: MonitoredValue, itemId: String?): UUID {
        val config = MonitorConfig(itemId, threshold, inertia)
        val monitor = createMonitor(type, UUID.randomUUID(), config)
        return if (validate(monitor)) {
            register(monitor)
            persist()
            monitor.id
        } else {
            throw IllegalArgumentException("Not mappable to device: $type with $itemId")
        }
    }

    fun update(monitorId: UUID, inertia: Duration?, threshold: MonitoredValue?): UUID {
        val oldMonitor = getById(monitorId)
        checkNotNull(oldMonitor) { "No monitor with id $monitorId was found" }
        check(inertia != null || threshold != null) { "Either inertia or threshold has to be provided" }
        val updatedConfig = MonitorConfig(
            oldMonitor.config.monitoredItemId,
            threshold ?: oldMonitor.config.threshold,
            inertia ?: oldMonitor.config.inertia
        )
        val updatedMonitor = createMonitor(oldMonitor.type, oldMonitor.id, updatedConfig)
        return if (validate(updatedMonitor)) {
            register(updatedMonitor)
            persist()
            updatedMonitor.id
        } else {
            throw IllegalArgumentException("Not mappable to device: ${oldMonitor.type} with ${oldMonitor.config.monitoredItemId}")
        }
    }

    fun remove(id: UUID): Boolean {
        val removed = activeMonitors.remove(id)

        removed?.let {
            persist()
            eventManager.removeEventsForMonitorId(id)
        }
        return removed != null
    }

    private fun reportItemMissingForMonitor(monitor: Monitor<MonitoredValue>, value: MonitoredValue?) {
        if (value == null) {
            logger.warn("Removing monitor ${monitor.id} as its item ${monitor.type.name}:${monitor.config.monitoredItemId} is missing")
            monitoredItemMissingChecker.reportItemMissing(monitor)
            remove(monitor.id)
        }
    }

    private fun persist() {
        val monitors = activeMonitors.map { it.value.second }.toList()
        repository.write(monitors)
    }

    private fun restore() {
        activeMonitors = mutableMapOf()
        repository.read().forEach { monitor ->
            register(monitor)
        }
    }

    private fun register(monitor: Monitor<MonitoredValue>) {
        val mechanism = MonitorMechanism(clock)
        activeMonitors[monitor.id] = mechanism to monitor
    }

    private fun validate(monitor: Monitor<MonitoredValue>): Boolean {
        return monitor.selectValue(
            MonitorInput(
                metrics.systemMetrics().systemLoad(),
                emptyList(),
                emptyList()
            )
        ) != null
    }

    private val containerTypes = listOf<Monitor.Type>(
        Monitor.Type.CONTAINER_RUNNING
    )

    private val containerStatisticsTypes = listOf<Monitor.Type>(
        Monitor.Type.CONTAINER_MEMORY_SPACE, Monitor.Type.CONTAINER_CPU_LOAD
    )

    private fun idsSubset(types: List<Monitor.Type>) =
        activeMonitors.filter { types.contains(it.value.second.type) }
            .mapNotNull { it.value.second.config.monitoredItemId }
}

