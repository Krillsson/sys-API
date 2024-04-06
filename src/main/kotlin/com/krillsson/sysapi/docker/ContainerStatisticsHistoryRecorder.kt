package com.krillsson.sysapi.docker

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.domain.docker.State
import com.krillsson.sysapi.core.history.ContainersHistoryRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ContainerStatisticsHistoryRecorder(
    config: YAMLConfigFile,
    private val containerManager: ContainerManager,
    private val historyRepository: ContainersHistoryRepository
) {

    private val historyConfiguration = config.metricsConfig.history

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    fun run() {
        val containers = containerManager.containers()
        val statistics = containers
            .filter { it.state == State.RUNNING }
            .mapNotNull { container ->
                containerManager.statsForContainer(container.id)
            }
        historyRepository.recordContainerStatistics(statistics)
        historyRepository.purgeContainerStatistics(historyConfiguration.purging.olderThan, historyConfiguration.purging.unit)
    }
}