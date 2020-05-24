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

import com.coxautodev.graphql.tools.SchemaParser
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.krillsson.sysapi.auth.BasicAuthenticator
import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.config.SystemApiConfiguration
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMixin
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryMetricQueryEvent
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.monitoring.*
import com.krillsson.sysapi.core.query.MetricQueryManager
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.graphql.MutationResolver
import com.krillsson.sysapi.graphql.QueryResolver
import com.krillsson.sysapi.graphql.scalars.ScalarTypes
import com.krillsson.sysapi.rest.*
import com.krillsson.sysapi.util.EnvironmentUtils
import com.krillsson.sysapi.util.OffsetDateTimeConverter
import com.krillsson.sysapi.util.Ticker
import com.krillsson.sysapi.util.Utils
import com.smoketurner.dropwizard.graphql.GraphQLBundle
import com.smoketurner.dropwizard.graphql.GraphQLFactory
import io.dropwizard.Application
import io.dropwizard.auth.AuthDynamicFeature
import io.dropwizard.auth.AuthValueFactoryProvider
import io.dropwizard.auth.basic.BasicCredentialAuthFilter
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.sslreload.SslReloadBundle
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature
import oshi.SystemInfo
import oshi.software.os.OperatingSystem
import java.net.NetworkInterface
import java.time.Clock
import java.util.*
import java.util.concurrent.Executors
import java.util.function.Supplier
import javax.servlet.DispatcherType
import javax.servlet.FilterRegistration


class SystemApiApplication : Application<SystemApiConfiguration>() {

    override fun getName(): String {
        return "system-API"
    }

    val queryResolver = QueryResolver()
    val mutationResolver = MutationResolver()

    override fun initialize(bootstrap: Bootstrap<SystemApiConfiguration>) {
        val mapper = bootstrap.objectMapper
        mapper.addMixInAnnotations(NetworkInterface::class.java, NetworkInterfaceMixin::class.java)
        val filterProvider = SimpleFilterProvider()
                .addFilter(
                        "networkInterface filter",
                        SimpleBeanPropertyFilter.serializeAllExcept(
                                "name",
                                "displayName",
                                "inetAddresses",
                                "interfaceAddresses",
                                "mtu",
                                "subInterfaces"
                        )
                )
        mapper.setFilters(filterProvider)
        bootstrap.addBundle(SslReloadBundle())
        val bundle: GraphQLBundle<SystemApiConfiguration> = object : GraphQLBundle<SystemApiConfiguration>() {
            override fun getGraphQLFactory(configuration: SystemApiConfiguration): GraphQLFactory? {
                val factory = configuration.graphQLFactory
                factory.setGraphQLSchema(
                        SchemaParser.newParser()
                                .file("schema.graphqls")
                                .resolvers(queryResolver,
                                        mutationResolver,
                                        queryResolver.systemInfoResolver,
                                        queryResolver.historyResolver,
                                        queryResolver.monitorResolver,
                                        queryResolver.monitorEventResolver,
                                        queryResolver.motherboardResolver,
                                        queryResolver.processorResolver,
                                        queryResolver.driveResolver,
                                        queryResolver.networkInterfaceResolver,
                                        queryResolver.memoryLoadResolver,
                                        queryResolver.processorMetricsResolver,
                                        queryResolver.driveMetricResolver,
                                        queryResolver.networkInterfaceMetricResolver
                                )
                                .scalars(ScalarTypes.scalars)
                                .build().makeExecutableSchema()
                )
                return factory
            }
        }
        bootstrap.addBundle(bundle)
    }

    @Throws(Exception::class)
    override fun run(config: SystemApiConfiguration, environment: Environment) {
        val cors: FilterRegistration.Dynamic = environment.servlets().addFilter("cors", CrossOriginFilter::class.java)
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")

        val systemInfo = SystemInfo()
        val hal = systemInfo.hardware
        val os = systemInfo.operatingSystem
        val eventBus = EventBus()

        if (config.forwardHttps()) {
            EnvironmentUtils.addHttpsForward(environment.applicationContext)
        }

        val ticker = Ticker(Executors.newScheduledThreadPool(
                1,
                ThreadFactoryBuilder()
                        .setNameFormat("tick-mgr-%d")
                        .build()
        ), 5)
        val speedMeasurementManager = SpeedMeasurementManager(
                Executors.newScheduledThreadPool(
                        1,
                        ThreadFactoryBuilder()
                                .setNameFormat("speed-mgr-%d")
                                .build()
                ), Clock.systemUTC(), 5)

        val provider = MetricsFactory(
                hal,
                os,
                SystemInfo.getCurrentPlatformEnum(),
                config,
                speedMeasurementManager,
                ticker
        ).create()

        val queryScheduledExecutor = Executors.newScheduledThreadPool(
                2,
                ThreadFactoryBuilder()
                        .setNameFormat("query-mgr-%d")
                        .build()
        )

        val historyMetricQueryManager = object : MetricQueryManager<HistoryMetricQueryEvent>(
                queryScheduledExecutor,
                config.metrics().history.interval,
                config.metrics().history.unit,
                provider,
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
                provider,
                eventBus
        ) {
            override fun event(load: SystemLoad): MonitorMetricQueryEvent {
                return MonitorMetricQueryEvent(load)
            }
        }

        val historyManager = MetricsHistoryManager(config.metrics().history, eventBus)

        val eventStore = EventStore(environment.objectMapper)
        val monitorStore = MonitorStore(environment.objectMapper)
        val eventManager = EventManager(eventStore)
        val monitorManager = MonitorManager(eventManager, eventBus, monitorStore, provider, com.krillsson.sysapi.util.Clock())

        queryResolver.monitorManager = monitorManager
        queryResolver.historyManager = historyManager
        queryResolver.metrics = provider
        queryResolver.os = os
        mutationResolver.historyManager = historyManager
        mutationResolver.metrics = provider
        setupManagedObjects(environment, monitorManager, eventManager, speedMeasurementManager, ticker, monitorMetricQueryManager, historyManager, historyMetricQueryManager)
        registerFeatures(environment, config.user())
        registerResources(environment, os, provider, historyManager, monitorManager, eventManager)
    }

    private fun setupManagedObjects(environment: Environment, monitorManager: MonitorManager, eventManager: EventManager, speedMeasurementManager: SpeedMeasurementManager, ticker: Ticker, monitorMetricQueryManager: MetricQueryManager<MonitorMetricQueryEvent>, historyManager: MetricsHistoryManager, historyMetricQueryManager: MetricQueryManager<HistoryMetricQueryEvent>) {
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
        environment.jersey().register(RolesAllowedDynamicFeature::class.java)
        environment.jersey().register(OffsetDateTimeConverter::class.java)
        environment.jersey().register(AuthDynamicFeature(userBasicCredentialAuthFilter))
        environment.jersey().register(AuthValueFactoryProvider.Binder(UserConfiguration::class.java))
    }

    private fun registerResources(environment: Environment, os: OperatingSystem, provider: Metrics, historyManager: MetricsHistoryManager, monitorManager: MonitorManager, eventManager: EventManager) {
        environment.jersey().register(SystemResource(os,
                SystemInfo.getCurrentPlatformEnum(),
                provider.cpuMetrics(),
                provider.networkMetrics(),
                provider.driveMetrics(),
                provider.memoryMetrics(),
                provider.processesMetrics(), provider.gpuMetrics(),
                provider.motherboardMetrics(),
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
                ))
    }

    companion object {
        private val LOGGER = org.slf4j.LoggerFactory.getLogger(SystemApiApplication::class.java)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SystemApiApplication().run(*args)
        }

    }


}
