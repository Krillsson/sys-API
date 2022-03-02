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

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.krillsson.sysapi.auth.BasicAuthenticator
import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.client.Clients
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryMetricQueryEvent
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.history.HistoryStore
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.MonitorMetricQueryEvent
import com.krillsson.sysapi.core.monitoring.MonitorRepository
import com.krillsson.sysapi.core.monitoring.MonitorStore
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.core.monitoring.event.EventRepository
import com.krillsson.sysapi.core.monitoring.event.EventStore
import com.krillsson.sysapi.core.query.MetricQueryManager
import com.krillsson.sysapi.core.speed.SpeedMeasurementManager
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.graphql.GraphQLBundle
import com.krillsson.sysapi.graphql.GraphQLConfiguration
import com.krillsson.sysapi.graphql.domain.Meta
import com.krillsson.sysapi.persistence.KeyValueRepository
import com.krillsson.sysapi.persistence.KeyValueStore
import com.krillsson.sysapi.persistence.Store
import com.krillsson.sysapi.rest.*
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.util.*
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
import java.time.Clock
import java.util.*
import java.util.concurrent.Executors
import java.util.function.Supplier
import javax.servlet.DispatcherType
import javax.servlet.FilterRegistration

class SysAPIApplication : Application<SysAPIConfiguration>() {

    override fun getName(): String {
        return "system-API"
    }

    val logger by logger()

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
    val graphqlConfiguration = GraphQLConfiguration()
    val graphqlBundle = GraphQLBundle(graphqlConfiguration)

    lateinit var eventStore: EventStore
    lateinit var monitorStore: MonitorStore
    lateinit var keyValueRepository: KeyValueRepository
    lateinit var historyStore: Store<List<SystemHistoryEntry>>
    lateinit var eventManager: EventManager
    lateinit var dockerClient: DockerClient
    lateinit var metricsFactory: MetricsFactory

    override fun initialize(bootstrap: Bootstrap<SysAPIConfiguration>) {
        val mapper = bootstrap.objectMapper
        mapper.registerKotlinModule()
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        eventStore = EventStore(mapper)
        monitorStore = MonitorStore(mapper)
        historyStore = HistoryStore(mapper)
        eventManager = EventManager(EventRepository(eventStore), Clock())
        keyValueRepository = KeyValueRepository(KeyValueStore(mapper))

        if(!FileSystem.data.isDirectory) {
            logger.info("Attempting to create {}", FileSystem.data)
            assert(FileSystem.data.mkdir()) {"Unable to create ${FileSystem.data}"}
        }

        bootstrap.addBundle(SslReloadBundle())
        bootstrap.addBundle(graphqlBundle)
    }

    @Throws(Exception::class)
    override fun run(config: SysAPIConfiguration, environment: Environment) {
        val cors: FilterRegistration.Dynamic =
            environment.servlets().addFilter("cors", CrossOriginFilter::class.java)
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")
        val clients = Clients(config.connectivityCheck.address)

        if (config.forwardHttps()) {
            EnvironmentUtils.addHttpsForward(environment.applicationContext)
        }

        dockerClient = DockerClient(config.metrics().cache, config.docker(), environment.objectMapper)
        metricsFactory = MetricsFactory(
            hal,
            os,
            SystemInfo.getCurrentPlatform(),
            speedMeasurementManager,
            ticker,
            ConnectivityCheckManager(clients.externalIpService, keyValueRepository, config.connectivityCheck)
        )

        val metrics = metricsFactory.get(config)

        val selfSignedCertificates = config.selfSignedCertificates()
        if(selfSignedCertificates.enabled){
            val certificateNamesCreator = CertificateNamesCreator(metrics.networkMetrics(), clients.externalIpService)
            val names = certificateNamesCreator.createNames(selfSignedCertificates)
            SelfSignedCertificateManager().start(names)
        }

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
                return MonitorMetricQueryEvent(load, dockerClient.listContainers())
            }
        }

        val historyManager = MetricsHistoryManager(config.metrics().history, eventBus, HistoryRepository(historyStore))
        val monitorManager = MonitorManager(
            eventManager,
            eventBus,
            MonitorRepository(monitorStore),
            metrics,
            com.krillsson.sysapi.util.Clock()
        )

        graphqlConfiguration.initialize(
            metrics,
            monitorManager,
            eventManager,
            historyManager,
            dockerClient,
            os.asOperatingSystem(),
            SystemInfo.getCurrentPlatform().asPlatform(),
            Meta(Utils.getVersionFromManifest(), os.processId)
        )
        registerManagedObjects(
            environment,
            monitorManager,
            eventManager,
            speedMeasurementManager,
            ticker,
            monitorMetricQueryManager,
            historyManager,
            historyMetricQueryManager,
            keyValueRepository
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
        historyMetricQueryManager: MetricQueryManager<HistoryMetricQueryEvent>,
        keyValueRepository: KeyValueRepository
    ) {
        environment.lifecycle().manage(monitorManager)
        environment.lifecycle().manage(eventManager)
        environment.lifecycle().manage(speedMeasurementManager)
        environment.lifecycle().manage(ticker)
        environment.lifecycle().manage(monitorMetricQueryManager)
        environment.lifecycle().manage(historyManager)
        environment.lifecycle().manage(historyMetricQueryManager)
        environment.lifecycle().manage(keyValueRepository)
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
        environment.jersey()
            .register(AuthValueFactoryProvider.Binder(UserConfiguration::class.java))
    }

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
        environment.jersey()
            .register(NetworkInterfacesResource(provider.networkMetrics(), historyManager))
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
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SysAPIApplication().run(*args)
        }
    }
}
