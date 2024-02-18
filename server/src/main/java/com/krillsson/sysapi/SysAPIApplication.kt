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
import com.krillsson.server.BuildConfig
import com.krillsson.sysapi.client.Clients
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.genericevents.GenericEventRepository
import com.krillsson.sysapi.core.genericevents.GenericEventStore
import com.krillsson.sysapi.core.history.*
import com.krillsson.sysapi.core.history.db.*
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.metrics.defaultimpl.DiskReadWriteRateMeasurementManager
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.MonitorRepository
import com.krillsson.sysapi.core.monitoring.MonitorStore
import com.krillsson.sysapi.core.monitoring.MonitoredItemMissingChecker
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.core.monitoring.event.EventRepository
import com.krillsson.sysapi.core.monitoring.event.EventStore
import com.krillsson.sysapi.docker.ContainerStatisticsHistoryRecorder
import com.krillsson.sysapi.docker.DockerClient
import com.krillsson.sysapi.docker.DockerManager
import com.krillsson.sysapi.graphql.GraphQLBundle
import com.krillsson.sysapi.graphql.GraphQLConfiguration
import com.krillsson.sysapi.graphql.domain.Meta
import com.krillsson.sysapi.logaccess.file.LogFilesManager
import com.krillsson.sysapi.logaccess.windowseventlog.WindowsManager
import com.krillsson.sysapi.mdns.Mdns
import com.krillsson.sysapi.periodictasks.*
import com.krillsson.sysapi.persistence.*
import com.krillsson.sysapi.systemd.SystemDaemonManager
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.updatechecker.UpdateChecker
import com.krillsson.sysapi.upnp.UpnpIgd
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

class SysAPIApplication : Application<SysAPIConfiguration>() {

    override fun getName(): String {
        return NAME
    }

    val logger by logger()

    private val hibernate: HibernateBundle<SysAPIConfiguration> = createHibernateBundle()
    private val flyWay: FlywayBundle<SysAPIConfiguration> = createFlywayBundle()

    private val systemInfo = SystemInfo()
    private val hal = systemInfo.hardware
    private val os = systemInfo.operatingSystem
    private val graphqlConfiguration = GraphQLConfiguration()
    private val graphqlBundle = GraphQLBundle(graphqlConfiguration)
    private val jobs: Map<TaskInterval, TaskExecutorJob> = mapOf(
        TaskInterval.VerySeldom to VerySeldomTasksJob(),
        TaskInterval.Seldom to SeldomTasksJob(),
        TaskInterval.LessOften to LessOftenTasksJob(),
        TaskInterval.Often to OftenTasksJob()
    )

    private lateinit var eventStore: EventStore
    private lateinit var monitorStore: MonitorStore
    private lateinit var genericEventRepository: GenericEventRepository
    private lateinit var genericEventStore: MemoryBackedStore<List<GenericEventStore.StoredGenericEvent>>
    private lateinit var keyValueRepository: KeyValueRepository
    private lateinit var eventManager: EventManager
    private lateinit var dockerClient: DockerClient
    private lateinit var dockerManager: DockerManager
    private lateinit var metricsFactory: MetricsFactory
    private lateinit var taskManager: TaskManager
    private lateinit var logFileManager: LogFilesManager
    private lateinit var systemDaemonManager: SystemDaemonManager
    private lateinit var windowsEventLogManager: WindowsManager

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
        genericEventStore = GenericEventStore(mapper).memoryBacked()
        genericEventRepository = GenericEventRepository(genericEventStore)
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
        val clients = Clients(environment.objectMapper, config.connectivityCheck.address, config.updateCheck.address)
        val historyDao = HistorySystemLoadDAO(hibernate.sessionFactory)
        val basicHistoryDao = BasicHistorySystemLoadDAO(hibernate.sessionFactory)
        val proxyFactory = UnitOfWorkAwareProxyFactory(hibernate)
        val migrator = proxyFactory
            .create(
                PersistenceMigrator::class.java,
                arrayOf(
                    SysAPIConfiguration::class.java,
                    FlywayBundle::class.java,
                    MetricRegistry::class.java
                ),
                arrayOf(config, flyWay, environment.metrics())
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
        logFileManager = LogFilesManager(config.logReader)
        windowsEventLogManager = WindowsManager(config.windows)
        systemDaemonManager = SystemDaemonManager(environment.objectMapper, config.linux)
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
        val dockerClient = DockerClient(
            config.docker,
            environment.objectMapper,
            SystemInfo.getCurrentPlatform().asPlatform()
        )


        val selfSignedCertificates = config.selfSignedCertificates
        if (selfSignedCertificates.enabled) {
            val certificateNamesCreator = CertificateNamesCreator(metrics.networkMetrics(), clients.externalIpService)
            SelfSignedCertificateManager().start(certificateNamesCreator, selfSignedCertificates)
        }
        UpdateChecker(
            config.updateCheck,
            genericEventRepository,
            clients.githubApiService,
            taskManager
        )
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
        val containerHistoryRepository = proxyFactory.create(
            /* clazz = */ ContainersHistoryRepository::class.java,
            /* constructorParamTypes = */ arrayOf(
                com.krillsson.sysapi.util.Clock::class.java,
                ContainerStatisticsDAO::class.java
            ),
            /* constructorArguments = */ arrayOf(
                com.krillsson.sysapi.util.Clock(),
                ContainerStatisticsDAO(hibernate.sessionFactory)
            )
        )
        dockerManager = DockerManager(
            dockerClient,
            config.docker.cache,
            containerHistoryRepository,
            config.docker
        )
        val historyManager = LegacyHistoryManager(historyRepository)
        val monitorManager = MonitorManager(
            taskManager,
            metrics,
            dockerManager,
            eventManager,
            MonitorRepository(monitorStore),
            MonitoredItemMissingChecker(genericEventRepository),
            com.krillsson.sysapi.util.Clock()
        )
        val historyRecorder = HistoryRecorder(taskManager, config.metricsConfig.history, metrics, historyRepository)
        val containerHistoryRecorder = ContainerStatisticsHistoryRecorder(
            taskManager,
            config.metricsConfig.history,
            dockerManager,
            containerHistoryRepository
        )
        environment.lifecycle().registerManagedObjects(
            monitorManager,
            eventManager,
            containerHistoryRecorder,
            historyRecorder,
            keyValueRepository,
            genericEventStore,
            Mdns(config, connectivityCheckManager),
            UpnpIgd(config)
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
            genericEventRepository,
            dockerManager,
            os.asOperatingSystem(),
            SystemInfo.getCurrentPlatform().asPlatform(),
            Meta(
                version = BuildConfig.APP_VERSION,
                buildDate = BuildConfig.BUILD_TIME.toString(),
                processId = os.processId,
                endpoints = endpoints.toList(),
            ),
            logFileManager,
            systemDaemonManager,
            windowsEventLogManager
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
