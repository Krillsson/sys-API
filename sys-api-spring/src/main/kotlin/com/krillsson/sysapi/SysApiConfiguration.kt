package com.krillsson.sysapi

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager
import com.krillsson.sysapi.core.connectivity.ExternalIpAddressService
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.MetricsFactory
import com.krillsson.sysapi.core.metrics.NetworkMetrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DiskReadWriteRateMeasurementManager
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager
import com.krillsson.sysapi.graphql.domain.Meta
import com.krillsson.sysapi.updatechecker.GithubApiService
import com.krillsson.sysapi.util.FileSystem
import com.krillsson.sysapi.util.asOperatingSystem
import com.krillsson.sysapi.util.asPlatform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.EnableTransactionManagement
import oshi.SystemInfo
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.time.Clock
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
class SysApiConfiguration {
    @Autowired
    var env: Environment? = null

    @Bean
    fun userConfigFile(): YAMLConfigFile {
        val configFile = File(FileSystem.config, "configuration.yml")
        check(configFile.exists()) {"Config file is missing at ${configFile.absoluteFile}"}
        val yamlParser = ObjectMapper(YAMLFactory())
        yamlParser.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        yamlParser.findAndRegisterModules()
        return yamlParser.readValue(configFile, YAMLConfigFile::class.java)
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
    fun clock(): Clock = Clock.systemUTC()

    @Bean
    fun hal() = SystemInfo().hardware

    @Bean
    fun os() = SystemInfo().operatingSystem

    @Bean
    fun operatingSystem() = SystemInfo().operatingSystem.asOperatingSystem()

    @Bean
    fun meta(os: OperatingSystem) = Meta(
            version = BuildConfig.APP_VERSION,
            buildDate = BuildConfig.BUILD_TIME.toString(),
            processId = os.processId,
            endpoints = emptyList(),
    )

    @Bean
    fun platform() = SystemInfo.getCurrentPlatform().asPlatform()

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

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}