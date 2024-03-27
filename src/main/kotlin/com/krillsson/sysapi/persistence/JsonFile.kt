package com.krillsson.sysapi.persistence

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.krillsson.sysapi.util.measureTimeMillis
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

abstract class JsonFile<T>(
    private val fileName: String,
    private val typeToken: TypeReference<T>,
    objectMapperBase: ObjectMapper
) : Store<T> {

    private val filePath = "data${System.getProperty("file.separator")}$fileName"

    private val objectMapper = objectMapperBase.copy()
        .apply {
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            dateFormat = StdDateFormat()
        }

    fun existsWithContent(): Boolean {
        val file = File(filePath)
        return file.exists() && Files.size(Paths.get(file.path)) > 0
    }

    override fun update(action: (T?) -> T) {
        val previousValue = read()
        val newValue = action(previousValue)
        write(newValue)
    }

    override fun read(): T? {
        val file = getFile()
        val timedValue: Pair<Long, T?> = measureTimeMillis {
            try {
                FileReader(file).use { reader ->
                    var jsonObject: T? = null
                    if (file.length() > 0) {
                        jsonObject = objectMapper.readValue(reader, typeToken)
                    }
                    jsonObject
                }
            } catch (e: IOException) {
                LOGGER.error("Exception while reading {}", filePath, e)
                null
            }
        }
        val duration = timedValue.first
        LOGGER.debug(
            "Took {} to read {}",
            "${duration}ms",
            filePath
        )
        return timedValue.second
    }

    override fun write(content: T) {
        val duration = measureTimeMillis {
            val file = getFile()
            try {
                FileWriter(file).use { writer ->
                    LOGGER.debug("Writing {}", filePath)
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, content)
                }
            } catch (e: IOException) {
                LOGGER.error("Exception while writing {}", filePath, e)
            }
        }
        LOGGER.debug(
            "Took {} to write {}",
            "${duration.first.toInt()}ms",
            filePath
        )
    }

    override fun clear() {
        LOGGER.debug("Deleting {}", filePath)
        getFile().delete()
    }

    fun deleteOnExit() {
        getFile().deleteOnExit()
        LOGGER.info("Marked $filePath for deletion. Will delete when application quits")
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