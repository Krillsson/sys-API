package com.krillsson.sysapi

import com.github.dockerjava.api.model.AuthConfig
import com.github.dockerjava.core.DockerConfigFile
import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.config.ConnectivityCheckConfiguration
import com.krillsson.sysapi.config.DockerConfiguration
import com.krillsson.sysapi.config.GraphQLPlayGroundConfiguration
import com.krillsson.sysapi.config.HistoryConfiguration
import com.krillsson.sysapi.config.HistoryPurgingConfiguration
import com.krillsson.sysapi.config.LinuxConfiguration
import com.krillsson.sysapi.config.LogReaderConfiguration
import com.krillsson.sysapi.config.MdnsConfiguration
import com.krillsson.sysapi.config.MetricsConfiguration
import com.krillsson.sysapi.config.MonitorConfiguration
import com.krillsson.sysapi.config.ProcessesConfiguration
import com.krillsson.sysapi.config.SelfSignedCertificateConfiguration
import com.krillsson.sysapi.config.UpdateCheckConfiguration
import com.krillsson.sysapi.config.UpnpIgdConfiguration
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.config.WindowsConfiguration
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.util.FileSystem
import com.krillsson.sysapi.util.logger
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportRuntimeHints
import oshi.util.GlobalConfig

@SpringBootApplication(scanBasePackages = ["com.krillsson.sysapi"])
@ImportRuntimeHints(RuntimeHints::class)
@RegisterReflectionForBinding(
    CacheConfiguration::class,
    ConnectivityCheckConfiguration::class,
    DockerConfiguration::class,
    GraphQLPlayGroundConfiguration::class,
    HistoryConfiguration::class,
    HistoryPurgingConfiguration::class,
    LinuxConfiguration::class,
    LogReaderConfiguration::class,
    MdnsConfiguration::class,
    MetricsConfiguration::class,
    MonitorConfiguration::class,
    ProcessesConfiguration::class,
    SelfSignedCertificateConfiguration::class,
    UpdateCheckConfiguration::class,
    UpnpIgdConfiguration::class,
    UserConfiguration::class,
    WindowsConfiguration::class,
    YAMLConfigFile::class,
    DockerConfigFile::class,
    AuthConfig::class,
    com.sun.jna.platform.mac.SystemB.Timeval::class
)
// https://www.graalvm.org/latest/reference-manual/native-image/dynamic-features/JNI/
// Failed to parse docker config.json
class SysAPIApplication {

    val logger by logger()

    @Bean
    fun postStartupHook(
        selfSignedCertificateManager: SelfSignedCertificateManager,
        certificateNamesCreator: CertificateNamesCreator,
        config: YAMLConfigFile
    ): ApplicationRunner =
        ApplicationRunner {
            GlobalConfig.set("oshi.os.windows.loadaverage", true)
            if (!FileSystem.data.isDirectory) {
                logger.info("Attempting to create {}", FileSystem.data)
                assert(FileSystem.data.mkdir()) { "Unable to create ${FileSystem.data}" }
            }
            selfSignedCertificateManager.start(certificateNamesCreator, config.selfSignedCertificates)
        }
}

fun main(args: Array<String>) {
    runApplication<SysAPIApplication>(*args)
}
