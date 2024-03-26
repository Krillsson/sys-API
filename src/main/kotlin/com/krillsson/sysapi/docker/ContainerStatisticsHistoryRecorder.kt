package com.krillsson.sysapi.docker

import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.history.ContainersHistoryRepository
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.TimeUnit

class ContainerStatisticsHistoryRecorder(
    private val configuration: HistoryConfiguration,
    private val containerManager: ContainerManager,
    private val historyRepository: ContainersHistoryRepository
) {
    /*
*
* often: 5s
lessOften: 15s
seldom:5min
verySeldom:30min
*
* */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    fun run() {
        val containers = containerManager.containers()
        val statistics = containers
            .filter { it.state == State.RUNNING }
            .mapNotNull { container ->
                containerManager.statsForContainer(container.id)
            }
        historyRepository.recordContainerStatistics(statistics)
        historyRepository.purgeContainerStatistics(configuration.purging.olderThan, configuration.purging.unit)
    }
}