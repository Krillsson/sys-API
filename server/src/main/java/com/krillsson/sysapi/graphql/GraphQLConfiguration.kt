package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.graphql.domain.DockerAvailable
import com.krillsson.sysapi.graphql.domain.DockerUnavailable
import com.krillsson.sysapi.graphql.mutations.PerformDockerContainerCommandOutputFailed
import com.krillsson.sysapi.graphql.mutations.PerformDockerContainerCommandOutputSucceeded
import com.krillsson.sysapi.graphql.mutations.UpdateMonitorOutputFailed
import com.krillsson.sysapi.graphql.mutations.UpdateMonitorOutputSucceeded
import com.krillsson.sysapi.graphql.scalars.ScalarTypes
import graphql.kickstart.tools.SchemaParser
import graphql.schema.GraphQLSchema

class GraphQLConfiguration {

    private val queryResolver = QueryResolver()
    private val mutationResolver = MutationResolver()

    fun createExecutableSchema(schemaFileName: String): GraphQLSchema {
        return SchemaParser.newParser()
            .file(schemaFileName)
            .resolvers(
                queryResolver,
                mutationResolver,
                queryResolver.dockerResolver,
                queryResolver.systemInfoResolver,
                queryResolver.historyResolver,
                queryResolver.monitorResolver,
                queryResolver.pastEventEventResolver,
                queryResolver.ongoingEventResolver,
                queryResolver.motherboardResolver,
                queryResolver.processorResolver,
                queryResolver.driveResolver,
                queryResolver.networkInterfaceResolver,
                queryResolver.memoryLoadResolver,
                queryResolver.memoryInfoResolver,
                queryResolver.processorMetricsResolver,
                queryResolver.driveMetricResolver,
                queryResolver.networkInterfaceMetricResolver
            )
            .dictionary(
                PerformDockerContainerCommandOutputSucceeded::class,
                PerformDockerContainerCommandOutputFailed::class,
                UpdateMonitorOutputSucceeded::class,
                UpdateMonitorOutputFailed::class,
                DockerUnavailable::class,
                DockerAvailable::class,
            )
            .scalars(ScalarTypes.scalars)
            .build()
            .makeExecutableSchema()
    }

    fun initialize(
        metrics: Metrics,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        historyManager: HistoryManager,
        dockerClient: DockerClient,
        operatingSystem: OperatingSystem,
        platform: Platform
    ) {
        queryResolver.initialize(
            metrics,
            monitorManager,
            eventManager,
            historyManager,
            dockerClient,
            operatingSystem,
            platform
        )
        mutationResolver.initialize(metrics, monitorManager, eventManager, historyManager, dockerClient)
    }
}