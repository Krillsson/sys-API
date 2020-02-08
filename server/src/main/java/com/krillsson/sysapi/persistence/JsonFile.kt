package com.krillsson.sysapi.persistence

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class JsonFile<T>(private val filePath: String, private val typeToken: TypeReference<T>, private val ifNull: T, private val objectMapper: ObjectMapper) {
    fun read(): T? {
        val file = getFile()
        try {
            FileReader(file).use { reader ->
                var jsonObject: T? = null
                if (file.length() > 0) {
                    jsonObject = objectMapper.readValue(reader, typeToken)
                }
                if (jsonObject == null) {
                    jsonObject = ifNull
                }
                return jsonObject
            }
        } catch (e: IOException) {
            LOGGER.error("Exception while deserializing", e)
        }
        return ifNull
    }

    fun write(content: T) {
        val file = getFile()
        FileWriter(file).use { writer ->
            LOGGER.debug("Writing {}", filePath)
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, content)
        }
    }

    fun <R> getPersistedData(persistChanges: Boolean, result: Result<T?, R>): R? {
        var value: R? = null
        val file = getFile()
        try {
            FileReader(file).use { reader ->
                var jsonObject: T? = null
                if (file.length() > 0) {
                    jsonObject = objectMapper.readValue(reader, typeToken)
                }
                if (jsonObject == null) {
                    jsonObject = ifNull
                }
                value = result.result(jsonObject)
                if (persistChanges) {
                    FileWriter(filePath).use { writer ->
                        LOGGER.debug("Writing {}", filePath)
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, jsonObject)
                    }
                }
            }
        } catch (e: IOException) {
            LOGGER.error("Exception while serializing/deserializing", e)
        }
        return value
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

    interface Result<T, R> {
        fun result(value: T): R
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JsonFile::class.java)
    }

}