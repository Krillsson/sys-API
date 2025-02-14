package com.krillsson.sysapi.util

import com.krillsson.sysapi.SPRING_CONFIG
import com.krillsson.sysapi.USER_CONFIG
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultConfigFileWriter : ApplicationRunner {

    val logger by logger()

    companion object {
        private val USER_CONFIG_FILE = File(FileSystem.config, USER_CONFIG)
        private val SPRING_CONFIG_FILE = File(FileSystem.config, SPRING_CONFIG)
        private const val DEFAULT_USER_CONFIG_PATH = "/config/${USER_CONFIG}"
        private const val DEFAULT_SPRING_CONFIG_PATH = "/config/${SPRING_CONFIG}"
    }


    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        FileSystem.assertConfigDirectory()
        writeDefaultFile(USER_CONFIG_FILE, DEFAULT_USER_CONFIG_PATH)
        writeDefaultFile(SPRING_CONFIG_FILE, DEFAULT_SPRING_CONFIG_PATH)
    }

    private fun writeDefaultFile(destinationFile: File, defaultUserConfigPath: String) {
        if (!destinationFile.exists()) {
            val fileStream = javaClass.getResourceAsStream(defaultUserConfigPath)
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

}