package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.auth.BasicAuthSecurityHandlerFactory
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.smoketurner.dropwizard.graphql.CachingPreparsedDocumentProvider
import com.smoketurner.dropwizard.graphql.GraphQLFactory
import graphql.execution.preparsed.PreparsedDocumentProvider
import graphql.kickstart.execution.GraphQLQueryInvoker
import graphql.kickstart.servlet.GraphQLHttpServlet
import io.dropwizard.core.ConfiguredBundle
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment

class GraphQLBundle(private val graphQLConfiguration: GraphQLConfiguration) : ConfiguredBundle<SysAPIConfiguration>,
    com.smoketurner.dropwizard.graphql.GraphQLConfiguration<SysAPIConfiguration> {

    override fun getGraphQLFactory(configuration: SysAPIConfiguration): GraphQLFactory {
        val factory = configuration.graphql
        factory.setGraphQLSchema(graphQLConfiguration.createExecutableSchema(factory.schemaFiles.first()))
        return factory
    }

    override fun initialize(bootstrap: Bootstrap<*>) {
        bootstrap.addBundle(GraphQLPlaygroundConfiguredBundle())
    }

    @Throws(java.lang.Exception::class)
    override fun run(configuration: SysAPIConfiguration, environment: Environment) {
        val factory = getGraphQLFactory(configuration)
        val provider: PreparsedDocumentProvider =
            CachingPreparsedDocumentProvider(factory.queryCache, environment.metrics())
        val schema = factory.build()
        val queryInvoker = GraphQLQueryInvoker.newBuilder()
            .withPreparsedDocumentProvider(provider)
            .withInstrumentation(factory.instrumentations)
            .build()
        val config = graphql.kickstart.servlet.GraphQLConfiguration
            .with(schema)
            .with(queryInvoker)
            .build()
        val servlet = GraphQLHttpServlet
            .with(config)
        environment.servlets().addServlet("graphql", servlet)
            .addMapping("/graphql", "/schema.json")
        val securityHandler = BasicAuthSecurityHandlerFactory(
            configuration.user.username,
            configuration.user.password
        ).create()
        environment.servlets().setSecurityHandler(securityHandler)
    }
}