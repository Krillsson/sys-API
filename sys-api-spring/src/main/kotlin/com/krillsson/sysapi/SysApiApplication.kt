package com.krillsson.sysapi

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.tls.CertificateNamesCreator
import com.krillsson.sysapi.tls.SelfSignedCertificateManager
import com.krillsson.sysapi.util.FileSystem
import com.krillsson.sysapi.util.logger
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import oshi.util.GlobalConfig

@SpringBootApplication(scanBasePackages = ["com.krillsson.sysapi"])
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
