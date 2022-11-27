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

import com.codahale.metrics.MetricRegistry
import com.google.common.eventbus.EventBus
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.krillsson.server.BuildConfig
import com.krillsson.sysapi.client.Clients
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.domain.history.SystemHistoryEntry
import com.krillsson.sysapi.core.domain.system.SystemLoad
import com.krillsson.sysapi.core.history.HistoryMetricQueryEvent
import com.krillsson.sysapi.core.history.HistoryRepository
import com.krillsson.sysapi.core.history.HistoryStore
import com.krillsson.sysapi.core.history.MetricsHistoryManager
import com.krillsson.sysapi.core.history.db.HistorySystemLoadDAO
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
import com.krillsson.sysapi.persistence.*
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.util.*
import io.dropwizard.Application
import io.dropwizard.flyway.FlywayBundle
import io.dropwizard.hibernate.HibernateBundle
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.sslreload.SslReloadBundle
import oshi.SystemInfo
import java.time.Clock
import java.util.concurrent.Executors


class SysAPIApplication : Application<SysAPIConfiguration>() {

    override fun getName(): String {
        return NAME
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
    private val hibernate: HibernateBundle<SysAPIConfiguration> = createHibernateBundle()
    private val flyWay: FlywayBundle<SysAPIConfiguration> = createFlywayBundle()

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

        if (!FileSystem.data.isDirectory) {
            logger.info("Attempting to create {}", FileSystem.data)
            assert(FileSystem.data.mkdir()) { "Unable to create ${FileSystem.data}" }
        }

        val mapper = bootstrap.objectMapper
        mapper.configure()
        bootstrap.addBundle(hibernate)
        bootstrap.addBundle(flyWay)

        eventStore = EventStore(mapper)
        monitorStore = MonitorStore(mapper)
        historyStore = HistoryStore(mapper)
        eventManager = EventManager(EventRepository(eventStore), Clock())
        keyValueRepository = KeyValueRepository(KeyValueStore(mapper))

        bootstrap.addBundle(SslReloadBundle())
        bootstrap.addBundle(graphqlBundle)
    }

    override fun run(config: SysAPIConfiguration, environment: Environment) {
        environment.healthChecks().disableHealthChecks()
        environment.metrics().disableMetrics()

        environment.jersey().registerFeatures(config.user)
        environment.servlets().configureCrossOriginFilter()
        if (config.forwardHttpToHttps) {
            EnvironmentUtils.addHttpsForward(environment.applicationContext)
        }
        val clients = Clients(config.connectivityCheck.address)
        val historyDao = HistorySystemLoadDAO(hibernate.sessionFactory)
        val proxyFactory = UnitOfWorkAwareProxyFactory(hibernate)
        val migrator = proxyFactory
            .create(
                PersistenceMigrator::class.java,
                arrayOf(
                    SysAPIConfiguration::class.java,
                    FlywayBundle::class.java,
                    HistorySystemLoadDAO::class.java,
                    HistoryStore::class.java,
                    MetricRegistry::class.java
                ),
                arrayOf(config, flyWay, historyDao, historyStore, environment.metrics())
            )
        migrator.migrate()

        dockerClient = DockerClient(config.metricsConfig.cache, config.docker, environment.objectMapper)
        metricsFactory = MetricsFactory(
            hal,
            os,
            SystemInfo.getCurrentPlatform(),
            speedMeasurementManager,
            ticker,
            ConnectivityCheckManager(clients.externalIpService, keyValueRepository, config.connectivityCheck)
        )

        val metrics = metricsFactory.get(config)

        val selfSignedCertificates = config.selfSignedCertificates
        if (selfSignedCertificates.enabled) {
            val certificateNamesCreator = CertificateNamesCreator(metrics.networkMetrics(), clients.externalIpService)
            SelfSignedCertificateManager().start(certificateNamesCreator, selfSignedCertificates)
        }

        val historyMetricQueryManager = object : MetricQueryManager<HistoryMetricQueryEvent>(
            queryScheduledExecutor,
            config.metricsConfig.history.interval,
            config.metricsConfig.history.unit,
            metrics,
            eventBus
        ) {
            override fun event(load: SystemLoad): HistoryMetricQueryEvent {
                return HistoryMetricQueryEvent(load)
            }
        }
        val monitorMetricQueryManager = object : MetricQueryManager<MonitorMetricQueryEvent>(
            queryScheduledExecutor,
            config.metricsConfig.monitor.interval,
            config.metricsConfig.monitor.unit,
            metrics,
            eventBus
        ) {
            override fun event(load: SystemLoad): MonitorMetricQueryEvent {
                return MonitorMetricQueryEvent(load, dockerClient.listContainers())
            }
        }
        val historyRepository = proxyFactory.create(
            HistoryRepository::class.java,
            HistorySystemLoadDAO::class.java,
            historyDao
        )
        val historyManager = MetricsHistoryManager(config.metricsConfig.history, eventBus, historyRepository)
        val monitorManager = MonitorManager(
            eventManager,
            eventBus,
            MonitorRepository(monitorStore),
            metrics,
            com.krillsson.sysapi.util.Clock()
        )
        environment.lifecycle().registerManagedObjects(
            monitorManager,
            eventManager,
            speedMeasurementManager,
            ticker,
            monitorMetricQueryManager,
            historyManager,
            historyMetricQueryManager,
            keyValueRepository
        )
        registerEndpoints(
            metrics,
            monitorManager,
            historyManager,
            environment
        )
    }

    private fun registerEndpoints(
        metrics: Metrics,
        monitorManager: MonitorManager,
        historyManager: MetricsHistoryManager,
        environment: Environment
    ) {
        graphqlConfiguration.initialize(
            metrics,
            monitorManager,
            eventManager,
            historyManager,
            dockerClient,
            os.asOperatingSystem(),
            SystemInfo.getCurrentPlatform().asPlatform(),
            Meta(BuildConfig.APP_VERSION, os.processId)
        )
        environment.jersey().registerJerseyResources(
            os,
            metrics,
            historyManager,
            monitorManager,
            eventManager,
            EnvironmentUtils.getEndpoints(environment)
        )
    }

    companion object {
        const val NAME = "system-API"

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SysAPIApplication().run(*args)
        }
    }
}
