package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.system.OperatingSystem
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.history.HistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
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
            .scalars(ScalarTypes.scalars)
            .build()
            .makeExecutableSchema()
    }

    fun initialize(metrics: Metrics, monitorManager: MonitorManager, eventManager: EventManager, historyManager: HistoryManager, operatingSystem: OperatingSystem, platform: Platform) {
        queryResolver.initialize(metrics, monitorManager, eventManager, historyManager, operatingSystem, platform)
        mutationResolver.initialize(metrics, monitorManager, eventManager, historyManager)
    }
}