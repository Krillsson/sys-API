package com.krillsson.sysapi.util

import com.krillsson.sysapi.SPRING_CONFIG
import com.krillsson.sysapi.USER_CONFIG
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream

@Component
class DefaultConfigFileWriter : ApplicationRunner {

    val logger by logger()


    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        FileSystem.assertConfigDirectory()
        val userConfig = File(FileSystem.config, USER_CONFIG)
        val springConfig = File(FileSystem.config, SPRING_CONFIG)

        if (!userConfig.exists()) {
            val fallbackUserConfigFile = javaClass.getResourceAsStream("/config/$USER_CONFIG")
            writeFile(fallbackUserConfigFile, userConfig)
        }

        if (!springConfig.exists()) {
            val fallbackSpringConfigFile = javaClass.getResourceAsStream("/config/$SPRING_CONFIG")
            writeFile(fallbackSpringConfigFile, springConfig)
        }

    }

    private fun writeFile(fileStream: InputStream?, destinationFile: File) {
        if (fileStream == null) {
            logger.warn("Resource not found, skipping: $destinationFile")
            return
        }

        logger.info("Writing default config to $destinationFile")

        destinationFile.outputStream().use { output ->
            fileStream.use { input ->
                input.copyTo(output)
            }
        }
    }


}