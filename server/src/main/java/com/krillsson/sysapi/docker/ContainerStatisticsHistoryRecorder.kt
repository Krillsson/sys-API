package com.krillsson.sysapi.docker

import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.history.ContainersHistoryRepository
import com.krillsson.sysapi.periodictasks.Task
import com.krillsson.sysapi.periodictasks.TaskInterval
import com.krillsson.sysapi.periodictasks.TaskManager
import io.dropwizard.lifecycle.Managed

class ContainerStatisticsHistoryRecorder(
    private val taskManager: TaskManager,
    private val configuration: HistoryConfiguration,
    private val containerManager: ContainerManager,
    private val historyRepository: ContainersHistoryRepository
) : Managed, Task {
    override val defaultInterval: TaskInterval = TaskInterval.VerySeldom
    override val key: Task.Key = Task.Key.StoreContainerStatisticsHistoryEntry

    override fun run() {
        val containers = containerManager.containers()
        val statistics = containers
            .filter { it.state == State.RUNNING }
            .mapNotNull { container ->
                containerManager.statsForContainer(container.id)
            }
        historyRepository.recordContainerStatistics(statistics)
        historyRepository.purgeContainerStatistics(configuration.purging.olderThan, configuration.purging.unit)
    }

    override fun start() {
        taskManager.registerTask(this)
    }
}