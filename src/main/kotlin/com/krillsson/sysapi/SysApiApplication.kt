package com.krillsson.sysapi

import com.github.dockerjava.api.model.AuthConfig
import com.github.dockerjava.core.DockerConfigFile
import com.krillsson.sysapi.config.*
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.util.FileSystem
import com.krillsson.sysapi.util.logger
import org.slf4j.LoggerFactory
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportRuntimeHints
import oshi.util.GlobalConfig
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

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
    val logger = LoggerFactory.getLogger(SysAPIApplication::class.java.name.removeSuffix("\$Companion"))
    val tmp = Path( System.getProperty("java.io.tmpdir"))
    val posixFilePermissions = Files.getPosixFilePermissions(tmp)
    val canExecute = posixFilePermissions.contains(PosixFilePermission.OWNER_EXECUTE)
    logger.info("${tmp.absolutePathString()} is ${if(canExecute) "executable" else "not executable"} all prms (${posixFilePermissions.joinToString { it.name }})")
    runApplication<SysAPIApplication>(*args)
}
