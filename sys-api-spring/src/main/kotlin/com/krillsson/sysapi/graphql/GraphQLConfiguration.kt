package com.krillsson.sysapi.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.graphql.domain.ConditionalValue
import com.krillsson.sysapi.graphql.domain.DockerAvailable
import com.krillsson.sysapi.graphql.domain.DockerUnavailable
import com.krillsson.sysapi.graphql.domain.FractionalValue
import com.krillsson.sysapi.graphql.domain.NumericalValue
import com.krillsson.sysapi.graphql.domain.ReadLogsForContainerOutputFailed
import com.krillsson.sysapi.graphql.domain.ReadLogsForContainerOutputSucceeded
import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessAvailable
import com.krillsson.sysapi.graphql.domain.SystemDaemonAccessUnavailable
import com.krillsson.sysapi.graphql.domain.WindowsManagementAccessAvailable
import com.krillsson.sysapi.graphql.domain.WindowsManagementAccessUnavailable
import com.krillsson.sysapi.graphql.mutations.PerformDockerContainerCommandOutputFailed
import com.krillsson.sysapi.graphql.mutations.PerformDockerContainerCommandOutputSucceeded
import com.krillsson.sysapi.graphql.mutations.PerformSystemDaemonCommandOutputFailed
import com.krillsson.sysapi.graphql.mutations.PerformSystemDaemonCommandOutputSucceeded
import com.krillsson.sysapi.graphql.mutations.PerformWindowsServiceCommandOutputFailed
import com.krillsson.sysapi.graphql.mutations.PerformWindowsServiceCommandOutputSucceeded
import com.krillsson.sysapi.graphql.mutations.UpdateMonitorOutputFailed
import com.krillsson.sysapi.graphql.mutations.UpdateMonitorOutputSucceeded
import com.krillsson.sysapi.graphql.scalars.ScalarTypes
import graphql.kickstart.execution.GraphQLObjectMapper
import graphql.kickstart.execution.GraphQLQueryInvoker
import graphql.kickstart.execution.config.GraphQLServletObjectMapperConfigurer
import graphql.kickstart.servlet.GraphQLConfiguration
import graphql.kickstart.servlet.GraphQLHttpServlet
import graphql.kickstart.servlet.input.GraphQLInvocationInputFactory
import graphql.kickstart.tools.GraphQLResolver
import graphql.kickstart.tools.SchemaParserBuilder
import graphql.kickstart.tools.SchemaParserOptions
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfiguration {

    @Bean
    fun createGraphQLServlet(
        resolvers: List<GraphQLResolver<*>>,
    ): ServletRegistrationBean<GraphQLHttpServlet> {
        val schema = SchemaParserBuilder()
            .files(
                files = *arrayOf(
                    "graphql/base.graphqls",
                    "graphql/docker.graphqls",
                    "graphql/logaccess.graphqls",
                    "graphql/systemdaemon.graphqls",
                    "graphql/monitoring.graphqls",
                    "graphql/system.graphqls",
                    "graphql/windows.graphqls",
                )
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
            .resolvers(resolvers = resolvers)
            .options(
                SchemaParserOptions.Builder()
                    .objectMapperConfigurer { objectMapper, _ ->
                        objectMapper.registerModule(JavaTimeModule())
                    }
                    .build()
            )
            .build()
            .makeExecutableSchema()
        val graphQLInvocationInputFactory = GraphQLInvocationInputFactory.newBuilder(schema)
            .build()
        val queryInvoker = GraphQLQueryInvoker.newBuilder()
            .build()

        return ServletRegistrationBean(
            GraphQLHttpServlet.with(
                GraphQLConfiguration
                    .with(graphQLInvocationInputFactory)
                    .with(queryInvoker)
                    .with(graphQLObjectMapper())
                    .build()
            ),
            "/graphql/*"
        ).apply {
            isAsyncSupported = false
        }
    }

    @Bean
    fun graphQLObjectMapper(): GraphQLObjectMapper =
        GraphQLObjectMapper.newBuilder()
            .withObjectMapperConfigurer(getObjectMapperConfigurer())
            .build()

    private fun getObjectMapperConfigurer(): GraphQLServletObjectMapperConfigurer {
        return GraphQLServletObjectMapperConfigurer { mapper: ObjectMapper ->
            mapper.registerModule(KotlinModule.Builder().build())
            mapper.registerModule(JavaTimeModule())
        }
    }
}