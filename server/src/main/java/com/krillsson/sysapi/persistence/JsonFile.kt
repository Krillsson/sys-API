package com.krillsson.sysapi.persistence

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

abstract class JsonFile<T>(private val filePath: String, private val typeToken: TypeReference<T>, private val objectMapper: ObjectMapper) : Store<T> {
    override fun read(): T? {
        val file = getFile()
        try {
            FileReader(file).use { reader ->
                var jsonObject: T? = null
                if (file.length() > 0) {
                    jsonObject = objectMapper.readValue(reader, typeToken)
                }
                return jsonObject
            }
        } catch (e: IOException) {
            LOGGER.error("Exception while reading", e)
        }
        return null
    }

    override fun write(content: T) {
        val file = getFile()
        try {
            FileWriter(file).use { writer ->
                LOGGER.debug("Writing {}", filePath)
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, content)
            }
        } catch (e: IOException) {
            LOGGER.error("Exception while writing", e)
        }
    }

    override fun clear() {
        LOGGER.debug("Deleting {}", filePath)
        getFile().delete()
    }

    private fun getFile(): File {
        val file = File(filePath)
        try { //does not create a file if it already exists
            file.createNewFile()
        } catch (e: IOException) {
            LOGGER.error("Unable to create file {}", filePath, e)
        }
        return file
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JsonFile::class.java)
    }

}