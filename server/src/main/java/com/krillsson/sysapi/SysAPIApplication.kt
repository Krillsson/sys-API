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
import com.krillsson.sysapi.core.history.*
import com.krillsson.sysapi.core.history.db.*
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.metrics.defaultimpl.DiskReadWriteRateMeasurementManager
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.MonitorRepository
import com.krillsson.sysapi.core.monitoring.MonitorStore
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.core.monitoring.event.EventRepository
import com.krillsson.sysapi.core.monitoring.event.EventStore
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.graphql.GraphQLBundle
import com.krillsson.sysapi.graphql.GraphQLConfiguration
import com.krillsson.sysapi.graphql.domain.Meta
import com.krillsson.sysapi.mdns.Mdns
import com.krillsson.sysapi.periodictasks.*
import com.krillsson.sysapi.persistence.*
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.util.*
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.dropwizard.core.sslreload.SslReloadBundle
import io.dropwizard.flyway.FlywayBundle
import io.dropwizard.hibernate.HibernateBundle
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory
import io.dropwizard.jobs.JobsBundle
import oshi.SystemInfo
import oshi.util.GlobalConfig
import java.util.concurrent.Executors


class SysAPIApplication : Application<SysAPIConfiguration>() {

    override fun getName(): String {
        return NAME
    }

    val logger by logger()

    val queryScheduledExecutor = Executors.newScheduledThreadPool(
        2,
        ThreadFactoryBuilder()
            .setNameFormat("query-mgr-%d")
            .build()
    )

    private val hibernate: HibernateBundle<SysAPIConfiguration> = createHibernateBundle()
    private val flyWay: FlywayBundle<SysAPIConfiguration> = createFlywayBundle()

    val systemInfo = SystemInfo()
    val hal = systemInfo.hardware
    val os = systemInfo.operatingSystem
    val eventBus = EventBus()
    val graphqlConfiguration = GraphQLConfiguration()
    val graphqlBundle = GraphQLBundle(graphqlConfiguration)
    val jobs: Map<TaskInterval, TaskExecutorJob> = mapOf(
        TaskInterval.VerySeldom to VerySeldomTasksJob(),
        TaskInterval.Seldom to SeldomTasksJob(),
        TaskInterval.LessOften to LessOftenTasksJob(),
        TaskInterval.Often to OftenTasksJob()
    )

    lateinit var eventStore: EventStore
    lateinit var monitorStore: MonitorStore
    lateinit var keyValueRepository: KeyValueRepository
    lateinit var historyStore: Store<List<StoredSystemHistoryEntry>>
    lateinit var eventManager: EventManager
    lateinit var dockerClient: DockerClient
    lateinit var metricsFactory: MetricsFactory
    lateinit var taskManager: TaskManager

    override fun initialize(bootstrap: Bootstrap<SysAPIConfiguration>) {
        GlobalConfig.set("oshi.os.windows.loadaverage", true)
        if (!FileSystem.data.isDirectory) {
            logger.info("Attempting to create {}", FileSystem.data)
            assert(FileSystem.data.mkdir()) { "Unable to create ${FileSystem.data}" }
        }

        bootstrap.addBundle(
            JobsBundle(
                *jobs.values.toTypedArray()
            )
        )
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
        val basicHistoryDao = BasicHistorySystemLoadDAO(hibernate.sessionFactory)
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

        taskManager = TaskManager(
            config.tasks,
            jobs
        )
        val diskReadWriteRateMeasurementManager =
            DiskReadWriteRateMeasurementManager(java.time.Clock.systemUTC(), taskManager)
        val networkUploadDownloadRateMeasurementManager =
            NetworkUploadDownloadRateMeasurementManager(java.time.Clock.systemUTC(), taskManager)


        dockerClient = DockerClient(config.metricsConfig.cache, config.docker, environment.objectMapper)
        val connectivityCheckManager = ConnectivityCheckManager(
            clients.externalIpService,
            keyValueRepository,
            config.connectivityCheck,
            taskManager
        )
        metricsFactory = MetricsFactory(
            hal,
            os,
            SystemInfo.getCurrentPlatform(),
            diskReadWriteRateMeasurementManager,
            networkUploadDownloadRateMeasurementManager,
            taskManager,
            connectivityCheckManager
        )

        val metrics = metricsFactory.get(config)

        val selfSignedCertificates = config.selfSignedCertificates
        if (selfSignedCertificates.enabled) {
            val certificateNamesCreator = CertificateNamesCreator(metrics.networkMetrics(), clients.externalIpService)
            SelfSignedCertificateManager().start(certificateNamesCreator, selfSignedCertificates)
        }
        val historyRepository = proxyFactory.create(
            /* clazz = */ HistoryRepository::class.java,
            /* constructorParamTypes = */ arrayOf(
                com.krillsson.sysapi.util.Clock::class.java,
                HistorySystemLoadDAO::class.java,
                BasicHistorySystemLoadDAO::class.java,
                CpuLoadDAO::class.java,
                MemoryLoadDAO::class.java,
                NetworkLoadDAO::class.java,
                DriveLoadDAO::class.java,
                DiskLoadDAO::class.java,
                FileSystemLoadDAO::class.java,
                ConnectivityDAO::class.java
            ),
            /* constructorArguments = */ arrayOf(
                com.krillsson.sysapi.util.Clock(),
                historyDao,
                basicHistoryDao,
                CpuLoadDAO(hibernate.sessionFactory),
                MemoryLoadDAO(hibernate.sessionFactory),
                NetworkLoadDAO(hibernate.sessionFactory),
                DriveLoadDAO(hibernate.sessionFactory),
                DiskLoadDAO(hibernate.sessionFactory),
                FileSystemLoadDAO(hibernate.sessionFactory),
                ConnectivityDAO(hibernate.sessionFactory)
            )
        )
        val historyManager = LegacyHistoryManager(historyRepository)
        val monitorManager = MonitorManager(
            taskManager,
            metrics,
            dockerClient,
            eventManager,
            MonitorRepository(monitorStore),
            metrics,
            com.krillsson.sysapi.util.Clock()
        )
        val historyRecorder = HistoryRecorder(taskManager, config.metricsConfig.history, metrics, historyRepository)
        environment.lifecycle().registerManagedObjects(
            monitorManager,
            eventManager,
            historyRecorder,
            keyValueRepository,
            Mdns(config, connectivityCheckManager)
        )
        registerEndpoints(
            metrics,
            monitorManager,
            historyManager,
            historyRepository,
            environment
        )
    }

    private fun registerEndpoints(
        metrics: Metrics,
        monitorManager: MonitorManager,
        historyManager: LegacyHistoryManager,
        historyRepository: HistoryRepository,
        environment: Environment
    ) {
        val endpoints = EnvironmentUtils.getEndpoints(environment)
        graphqlConfiguration.initialize(
            metrics,
            monitorManager,
            eventManager,
            historyRepository,
            dockerClient,
            os.asOperatingSystem(),
            SystemInfo.getCurrentPlatform().asPlatform(),
            Meta(
                version = BuildConfig.APP_VERSION,
                buildDate = BuildConfig.BUILD_TIME.toString(),
                processId = os.processId,
                endpoints = endpoints.toList(),
            )
        )
        environment.jersey().registerJerseyResources(
            os,
            metrics,
            historyManager,
            monitorManager,
            eventManager,
            endpoints
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
