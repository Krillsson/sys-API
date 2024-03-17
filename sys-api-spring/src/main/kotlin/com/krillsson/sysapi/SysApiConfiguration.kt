package com.krillsson.sysapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.connectivity.ExternalIpAddressService
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DiskReadWriteRateMeasurementManager
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import com.krillsson.sysapi.updatechecker.GithubApiService
import com.krillsson.sysapi.util.FileSystem
import com.krillsson.sysapi.util.asPlatform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import oshi.PlatformEnum
import oshi.SystemInfo
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Clock
import java.util.Properties
import javax.sql.DataSource

@Configuration
class SysApiConfiguration {
    @Autowired
    var env: Environment? = null

    @Bean
    fun dataSource(): DataSource? {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.sqlite.JDBC")
        dataSource.setUrl("jdbc:sqlite:${FileSystem.data.absolutePath}/database.sqlite")
        return dataSource
    }

    @Bean
    fun metrics(
        configuration: YAMLConfigFile,
        hal: HardwareAbstractionLayer,
        operatingSystem: OperatingSystem,
        platform: Platform,
        diskReadWriteRateMeasurementManager: DiskReadWriteRateMeasurementManager,
        networkUploadDownloadRateMeasurementManager: NetworkUploadDownloadRateMeasurementManager,
        connectivityCheckManager: ConnectivityCheckManager
    ): Metrics {
        return MetricsFactory(
            hal,
            operatingSystem,
            platform,
            diskReadWriteRateMeasurementManager,
            networkUploadDownloadRateMeasurementManager,
            connectivityCheckManager
        ).get(configuration)
    }

    @Bean
    fun networkMetrics(metrics: Metrics): NetworkMetrics = metrics.networkMetrics()

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource()!!
        em.setPackagesToScan(*arrayOf("com.baeldung.books.models"))
        em.jpaVendorAdapter = HibernateJpaVendorAdapter()
        em.setJpaProperties(additionalProperties())
        return em
    }

    @Bean
    fun clock(): Clock = Clock.systemUTC()

    @Bean
    fun hal() = SystemInfo().hardware

    @Bean
    fun os() = SystemInfo().operatingSystem

    @Bean
    fun platform() = SystemInfo.getCurrentPlatform().asPlatform()

    fun additionalProperties(): Properties {
        val hibernateProperties = Properties()
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop")
        return hibernateProperties
    }

    @Bean
    fun githubApi(yamlConfigFile: YAMLConfigFile, mapper: ObjectMapper): GithubApiService {
        return Retrofit.Builder()
            .baseUrl(yamlConfigFile.updateCheck.address)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()
            .create(GithubApiService::class.java)
    }

    @Bean
    fun externalIpAddressService(yamlConfigFile: YAMLConfigFile): ExternalIpAddressService {
        return Retrofit.Builder()
            .baseUrl(yamlConfigFile.connectivityCheck.address)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(ExternalIpAddressService::class.java)
    }
}