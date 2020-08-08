/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi

import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.krillsson.sysapi.auth.BasicAuthenticator
import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryMetricQueryEvent
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.monitoring.EventManager
import com.krillsson.sysapi.core.monitoring.EventStore
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorStore
import com.krillsson.sysapi.core.query.MetricQueryManager
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.graphql.GraphQLConfiguration
import com.krillsson.sysapi.rest.CpuResource
import com.krillsson.sysapi.rest.DrivesResource
import com.krillsson.sysapi.rest.EventResource
import com.krillsson.sysapi.rest.GpuResource
import com.krillsson.sysapi.rest.MemoryResource
import com.krillsson.sysapi.rest.MetaInfoResource
import com.krillsson.sysapi.rest.MonitorResource
import com.krillsson.sysapi.rest.MotherboardResource
import com.krillsson.sysapi.rest.NetworkInterfacesResource
import com.krillsson.sysapi.rest.ProcessesResource
import com.krillsson.sysapi.rest.SystemResource
import com.krillsson.sysapi.util.EnvironmentUtils
import com.krillsson.sysapi.util.OffsetDateTimeConverter
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Utils
import com.smoketurner.dropwizard.graphql.CachingPreparsedDocumentProvider
import com.smoketurner.dropwizard.graphql.GraphQLFactory
import graphql.execution.preparsed.PreparsedDocumentProvider
import graphql.kickstart.execution.GraphQLQueryInvoker
import graphql.kickstart.servlet.GraphQLHttpServlet
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.ConfiguredBundle
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.auth.Auth
import io.dropwizard.auth.AuthDynamicFeature
import io.dropwizard.auth.AuthValueFactoryProvider
import io.dropwizard.auth.basic.BasicCredentialAuthFilter
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.sslreload.SslReloadBundle
import org.eclipse.jetty.security.ConstraintMapping
import org.eclipse.jetty.security.ConstraintSecurityHandler
import org.eclipse.jetty.security.HashLoginService
import org.eclipse.jetty.security.SecurityHandler
import org.eclipse.jetty.security.UserStore
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.eclipse.jetty.util.security.Constraint
import org.eclipse.jetty.util.security.Credential
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature
import org.glassfish.jersey.server.model.AnnotatedMethod
import oshi.SystemInfo
import oshi.software.os.OperatingSystem
import java.io.IOException
import java.time.Clock
import java.util.EnumSet
import java.util.Objects
import java.util.Optional
import java.util.concurrent.Executors
import java.util.function.Supplier
import javax.annotation.Priority
import javax.annotation.security.DenyAll
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.servlet.DispatcherType
import javax.servlet.FilterRegistration
import javax.ws.rs.Priorities
import javax.ws.rs.WebApplicationException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.DynamicFeature
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.FeatureContext

class SysAPIApplication : Application<SysAPIConfiguration>() {

    override fun getName(): String {
        return "system-API"
    }

    val speedMeasurementManager = SpeedMeasurementManager(
        Executors.newScheduledThreadPool(
            1,
            ThreadFactoryBuilder()
                .setNameFormat("speed-mgr-%d")
                .build()
        ), Clock.systemUTC(), 5
    )

    val queryScheduledExecutor = Executors.newScheduledThreadPool(
        2,
        ThreadFactoryBuilder()
            .setNameFormat("query-mgr-%d")
            .build()
    )

    val ticker = Ticker(
        Executors.newScheduledThreadPool(
            1,
            ThreadFactoryBuilder()
                .setNameFormat("tick-mgr-%d")
                .build()
        ), 5
    )

    val systemInfo = SystemInfo()
    val hal = systemInfo.hardware
    val os = systemInfo.operatingSystem
    val eventBus = EventBus()
    val metricsFactory = MetricsFactory(hal, os, SystemInfo.getCurrentPlatformEnum(), speedMeasurementManager, ticker)
    val graphQLConfiguration = GraphQLConfiguration()

    lateinit var eventStore: EventStore
    lateinit var monitorStore: MonitorStore
    lateinit var eventManager: EventManager

    override fun initialize(bootstrap: Bootstrap<SysAPIConfiguration>) {
        val mapper = bootstrap.objectMapper

        eventStore = EventStore(mapper)
        monitorStore = MonitorStore(mapper)
        eventManager = EventManager(eventStore)

        bootstrap.addBundle(SslReloadBundle())
        val bundle: GraphQLBundle<SysAPIConfiguration> = object : GraphQLBundle<SysAPIConfiguration>() {
            override fun getGraphQLFactory(configuration: SysAPIConfiguration): GraphQLFactory? {
                val factory = configuration.graphQLFactory
                factory.setGraphQLSchema(graphQLConfiguration.createExecutableSchema(factory.schemaFiles.first()))
                return factory
            }
        }
        bootstrap.addBundle(bundle)
    }

    abstract class GraphQLBundle<C : Configuration?> : ConfiguredBundle<C>,
        com.smoketurner.dropwizard.graphql.GraphQLConfiguration<C> {
        override fun initialize(bootstrap: Bootstrap<*>) {
            bootstrap.addBundle(AssetsBundle("/assets", "/", "index.htm", "graphql-playground"))
        }

        @Throws(java.lang.Exception::class)
        override fun run(configuration: C, environment: Environment) {
            val factory = getGraphQLFactory(configuration)
            val provider: PreparsedDocumentProvider =
                CachingPreparsedDocumentProvider(factory.queryCache, environment.metrics())
            val schema = factory.build()
            val queryInvoker =
                GraphQLQueryInvoker.newBuilder().withPreparsedDocumentProvider(provider)
                    .withInstrumentation(factory.instrumentations).build()
            val config =
                graphql.kickstart.servlet.GraphQLConfiguration.with(schema).with(queryInvoker).build()
            val servlet = GraphQLHttpServlet.with(config)
            environment.servlets().addServlet("graphql", servlet)
                .addMapping(*arrayOf("/graphql", "/schema.json"))
            environment.servlets().setSecurityHandler(basicAuth("user", "password"))
        }

        fun basicAuth(username: String, password: String): SecurityHandler {
            val l = HashLoginService()
            val userStore: UserStore = UserStore()
            userStore.addUser(username, Credential.getCredential(password), arrayOf("AUTHENTICATED"))
            l.setUserStore(userStore)
            l.setName("system-API")
            val constraint = Constraint()
            constraint.setName(Constraint.__BASIC_AUTH)
            constraint.setRoles(arrayOf("AUTHENTICATED"))
            constraint.setAuthenticate(true)
            val cm = ConstraintMapping()
            cm.setConstraint(constraint)
            cm.setPathSpec("/graphql")
            val csh = ConstraintSecurityHandler()
            csh.setAuthenticator(org.eclipse.jetty.security.authentication.BasicAuthenticator())
            csh.setRealmName("system-API")
            csh.addConstraintMapping(cm)
            csh.setLoginService(l)
            return csh
        }
    }

    @Throws(Exception::class)
    override fun run(config: SysAPIConfiguration, environment: Environment) {
        val cors: FilterRegistration.Dynamic = environment.servlets().addFilter("cors", CrossOriginFilter::class.java)
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")

        if (config.forwardHttps()) {
            EnvironmentUtils.addHttpsForward(environment.applicationContext)
        }

        val metrics = metricsFactory.get(config)

        val historyMetricQueryManager = object : MetricQueryManager<HistoryMetricQueryEvent>(
            queryScheduledExecutor,
            config.metrics().history.interval,
            config.metrics().history.unit,
            metrics,
            eventBus
        ) {
            override fun event(load: SystemLoad): HistoryMetricQueryEvent {
                return HistoryMetricQueryEvent(load)
            }
        }
        val monitorMetricQueryManager = object : MetricQueryManager<MonitorMetricQueryEvent>(
            queryScheduledExecutor,
            config.metrics().monitor.interval,
            config.metrics().monitor.unit,
            metrics,
            eventBus
        ) {
            override fun event(load: SystemLoad): MonitorMetricQueryEvent {
                return MonitorMetricQueryEvent(load)
            }
        }

        val historyManager = MetricsHistoryManager(config.metrics().history, eventBus)
        val monitorManager =
            MonitorManager(eventManager, eventBus, monitorStore, metrics, com.krillsson.sysapi.util.Clock())

        graphQLConfiguration.initialize(metrics, monitorManager, eventManager, historyManager)
        registerManagedObjects(
            environment,
            monitorManager,
            eventManager,
            speedMeasurementManager,
            ticker,
            monitorMetricQueryManager,
            historyManager,
            historyMetricQueryManager
        )
        registerFeatures(environment, config.user())
        registerResources(environment, os, metrics, historyManager, monitorManager, eventManager)
    }

    private fun registerManagedObjects(
        environment: Environment,
        monitorManager: MonitorManager,
        eventManager: EventManager,
        speedMeasurementManager: SpeedMeasurementManager,
        ticker: Ticker,
        monitorMetricQueryManager: MetricQueryManager<MonitorMetricQueryEvent>,
        historyManager: MetricsHistoryManager,
        historyMetricQueryManager: MetricQueryManager<HistoryMetricQueryEvent>
    ) {
        environment.lifecycle().manage(monitorManager)
        environment.lifecycle().manage(eventManager)
        environment.lifecycle().manage(speedMeasurementManager)
        environment.lifecycle().manage(ticker)
        environment.lifecycle().manage(monitorMetricQueryManager)
        environment.lifecycle().manage(historyManager)
        environment.lifecycle().manage(historyMetricQueryManager)
    }

    private fun registerFeatures(environment: Environment, userConfig: UserConfiguration) {
        val userBasicCredentialAuthFilter = BasicCredentialAuthFilter.Builder<UserConfiguration>()
            .setAuthenticator(BasicAuthenticator(userConfig))
            .setRealm(name)
            .setAuthorizer(BasicAuthorizer(userConfig))
            .buildAuthFilter()
        //environment.jersey().register(GraphQLAuthDynamicFeature(userBasicCredentialAuthFilter))
        environment.jersey().register(RolesAllowedDynamicFeature::class.java)
        environment.jersey().register(OffsetDateTimeConverter::class.java)
        environment.jersey().register(AuthDynamicFeature(userBasicCredentialAuthFilter))
        environment.jersey().register(AuthValueFactoryProvider.Binder(UserConfiguration::class.java))
    }

    /*class GraphQLAuthDynamicFeature(private val authFilter: ContainerRequestFilter) : DynamicFeature {
        override fun configure(
            resourceInfo: ResourceInfo,
            context: FeatureContext
        ) {
            val am =
                AnnotatedMethod(resourceInfo.resourceMethod)
            val parameterAnnotations = am.parameterAnnotations
            val parameterTypes = am.parameterTypes

            // First, check for any @Auth annotations on the method.
            for (i in parameterAnnotations.indices) {
                for (annotation in parameterAnnotations[i]) {
                    if (annotation is Auth) {
                        // Optional auth requires that a concrete AuthFilter be provided.
                        if (parameterTypes[i] == Optional::class.java && authFilter != null) {
                            context.register(WebApplicationExceptionCatchingFilter(authFilter))
                            return
                        } else {
                            registerAuthFilter(context)
                            return
                        }
                    }
                }
            }

            // Second, check for any authorization annotations on the class or method.
            // Note that @DenyAll shouldn't be attached to classes.
            val annotationOnClass =
                resourceInfo.resourceClass.getAnnotation(RolesAllowed::class.java) != null ||
                    resourceInfo.resourceClass.getAnnotation(PermitAll::class.java) != null
            val annotationOnMethod =
                am.isAnnotationPresent(RolesAllowed::class.java) || am.isAnnotationPresent(
                    DenyAll::class.java
                ) ||
                    am.isAnnotationPresent(PermitAll::class.java)
            if (annotationOnClass || annotationOnMethod) {
                registerAuthFilter(context)
            }
        }

        private fun registerAuthFilter(context: FeatureContext) {
            context.register(authFilter)
        }
    }

    @Priority(Priorities.AUTHENTICATION)
    internal class WebApplicationExceptionCatchingFilter(underlying: ContainerRequestFilter) :
        ContainerRequestFilter {
        private val underlying: ContainerRequestFilter

        @Throws(IOException::class)
        override fun filter(requestContext: ContainerRequestContext) {
            try {
                underlying.filter(requestContext)
            } catch (err: WebApplicationException) {
                // Pass through.
            }
        }

        init {
            Objects.requireNonNull(underlying, "Underlying ContainerRequestFilter is not set")
            this.underlying = underlying
        }
    }*/

    private fun registerResources(
        environment: Environment,
        os: OperatingSystem,
        provider: Metrics,
        historyManager: MetricsHistoryManager,
        monitorManager: MonitorManager,
        eventManager: EventManager
    ) {
        environment.jersey().register(SystemResource(
            provider.systemMetrics(),
            historyManager, Supplier { os.systemUptime }
        ))
        environment.jersey().register(DrivesResource(provider.driveMetrics(), historyManager))
        environment.jersey().register(GpuResource(provider.gpuMetrics(), historyManager))
        environment.jersey().register(MemoryResource(provider.memoryMetrics(), historyManager))
        environment.jersey().register(NetworkInterfacesResource(provider.networkMetrics(), historyManager))
        environment.jersey().register(ProcessesResource(provider.processesMetrics()))
        environment.jersey().register(CpuResource(provider.cpuMetrics(), historyManager))
        environment.jersey().register(MotherboardResource(provider.motherboardMetrics()))
        environment.jersey().register(EventResource(eventManager))
        environment.jersey().register(MonitorResource(monitorManager, eventManager))
        environment.jersey().register(
            MetaInfoResource(
                Utils.getVersionFromManifest(),
                EnvironmentUtils.getEndpoints(environment),
                os.processId
            )
        )
    }

    companion object {
        private val LOGGER = org.slf4j.LoggerFactory.getLogger(SysAPIApplication::class.java)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SysAPIApplication().run(*args)
        }
    }
}
