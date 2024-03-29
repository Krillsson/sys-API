package com.krillsson.sysapi.graphql

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.core.genericevents.GenericEventRepository
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.docker.ContainerManager
import com.krillsson.sysapi.graphql.domain.*
import com.krillsson.sysapi.graphql.mutations.*
import com.krillsson.sysapi.graphql.scalars.ScalarTypes
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsManager
import com.krillsson.sysapi.systemd.SystemDaemonManager
import graphql.kickstart.tools.SchemaParser
import graphql.kickstart.tools.SchemaParserOptions
import graphql.schema.GraphQLSchema

class GraphQLConfiguration {

    private val queryResolver = QueryResolver()
    private val mutationResolver = MutationResolver()

    fun createExecutableSchema(
        vararg schemaFileNames: String
    ): GraphQLSchema {
        return SchemaParser.newParser()
            .options(
                SchemaParserOptions.Builder()
                    .objectMapperConfigurer { objectMapper, _ ->
                        objectMapper.registerModule(JavaTimeModule())
                    }
                    .build()
            )
            .files(*schemaFileNames)
            .resolvers(
                queryResolver,
                mutationResolver,
                queryResolver.dockerResolver,
                queryResolver.containerResolver,
                queryResolver.systemInfoResolver,
                queryResolver.historyResolver,
                queryResolver.pastEventEventResolver,
                queryResolver.ongoingEventResolver,
                queryResolver.monitoredItemMissingGenericEventResolver,
                queryResolver.updateAvailableGenericEventResolver,
                queryResolver.motherboardResolver,
                queryResolver.processorResolver,
                queryResolver.diskResolver,
                queryResolver.fileSystemResolver,
                queryResolver.diskMetricResolver,
                queryResolver.networkInterfaceResolver,
                queryResolver.memoryLoadResolver,
                queryResolver.memoryInfoResolver,
                queryResolver.processorMetricsResolver,
                queryResolver.networkInterfaceMetricResolver,
                queryResolver.monitorResolver,
            )
            .dictionary(
                PerformDockerContainerCommandOutputSucceeded::class,
                PerformDockerContainerCommandOutputFailed::class,
                UpdateMonitorOutputSucceeded::class,
                UpdateMonitorOutputFailed::class,
                DockerUnavailable::class,
                DockerAvailable::class,
                SystemDaemonAccessAvailable::class,
                SystemDaemonAccessUnavailable::class,
                PerformSystemDaemonCommandOutputSucceeded::class,
                PerformSystemDaemonCommandOutputFailed::class,
                PerformWindowsServiceCommandOutputSucceeded::class,
                PerformWindowsServiceCommandOutputFailed::class,
                WindowsManagementAccessAvailable::class,
                WindowsManagementAccessUnavailable::class,
                GenericEvent.UpdateAvailable::class,
                GenericEvent.MonitoredItemMissing::class,
                ReadLogsForContainerOutputSucceeded::class,
                ReadLogsForContainerOutputFailed::class,
                NumericalValue::class,
                FractionalValue::class,
                ConditionalValue::class
            )
            .scalars(ScalarTypes.scalars)
            .build()
            .makeExecutableSchema()
    }

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        historyManager: HistoryRepository,
        genericEventRepository: GenericEventRepository,
        containerManager: ContainerManager,
        operatingSystem: OperatingSystem,
        platform: Platform,
        meta: Meta,
        logFileManager: LogFilesManager,
        systemDaemonManager: SystemDaemonManager,
        windowsManager: WindowsManager
    ) {
        queryResolver.initialize(
            metrics,
            monitorManager,
            eventManager,
            historyManager,
            genericEventRepository,
            containerManager,
            operatingSystem,
            platform,
            meta,
            systemDaemonManager,
            logFileManager,
            windowsManager
        )
        mutationResolver.initialize(
            metrics,
            monitorManager,
            genericEventRepository,
            eventManager,
            containerManager,
            systemDaemonManager,
            windowsManager
        )
    }
}