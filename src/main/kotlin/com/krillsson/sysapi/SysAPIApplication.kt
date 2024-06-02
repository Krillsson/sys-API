package com.krillsson.sysapi

import com.github.dockerjava.api.model.AuthConfig
import com.github.dockerjava.core.DockerConfigFile
import com.krillsson.sysapi.config.*
import com.krillsson.sysapi.core.genericevents.GenericEventStore
import com.krillsson.sysapi.core.monitoring.MonitorStore
import com.krillsson.sysapi.core.monitoring.event.EventStore
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
@ImportRuntimeHints(RuntimeHint::class)
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
    GenericEventStore.StoredGenericEvent.UpdateAvailable::class,
    GenericEventStore.StoredGenericEvent.MonitoredItemMissing::class,
    EventStore.StoredEvent::class,
    MonitorStore.StoredMonitor::class,
    AuthConfig::class
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
