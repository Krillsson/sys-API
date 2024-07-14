package com.krillsson.sysapi.util

import java.io.File


object FileSystem {
    val data = File("data")
    val config = File("config")

    val logger by logger()

    fun assertDataDirectory(){
        if (!data.isDirectory) {
            logger.info("Attempting to create directory {}", data.absolutePath)
            assert(data.mkdir()) { "Unable to create ${data.absolutePath}" }
        }
    }
}