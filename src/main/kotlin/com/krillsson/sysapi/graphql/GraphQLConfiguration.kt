package com.krillsson.sysapi.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.graphql.domain.*
import com.krillsson.sysapi.graphql.mutations.*
import com.krillsson.sysapi.graphql.scalars.ScalarTypes
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/*@Configuration
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
                "/graphql"
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
}*/