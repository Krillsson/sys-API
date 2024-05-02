package com.krillsson.sysapi.core.webservicecheck

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.util.logger
import okhttp3.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.jvm.optionals.getOrNull


@Service
@Transactional
class WebServerCheckService(
    private val repository: WebServerCheckRepository,
    private val historyRepository: WebServerHistoryEntryRepository,
    yamlConfigFile: YAMLConfigFile,
) {
    private val logger by logger()

    private val client = OkHttpClient.Builder()
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build();
    private val purgeConfig = yamlConfigFile.metricsConfig.history.purging

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    fun run() {
        checkAvailabilities()
        purgeOutOfDateEntries()
    }

    fun checkNow(url: String): OneOffWebserverResult {
        return try {
            val request: Request = Request.Builder()
                .url(url)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            logger.debug("Request ${if (response.isSuccessful) "SUCCESS" else "FAIL"} $url: ${response.code} - ${response.message}")
            OneOffWebserverResult(
                Instant.now(),
                response.code,
                response.message,
                if (response.isSuccessful) response.readLimitedBody() else null
            )
        } catch (e: Exception) {
            val errorMessage = e.message ?: e::class.java.simpleName
            logger.warn("Request FAIL ${url}: $errorMessage")
            OneOffWebserverResult(
                Instant.now(),
                -1,
                errorMessage,
                null
            )
        }
    }

    fun addWebServer(url: String): AddWebServerResult {
        return when (val validationResult = validateUrl(url)) {
            URLValidationResult.Valid -> {
                val id = UUID.randomUUID()
                repository.save(WebServerCheckEntity(id, url))
                logger.debug("Added $url webserver check")
                AddWebServerResult.Success(id)
            }

            is URLValidationResult.Invalid -> {
                logger.warn("URL $url is invalid: ${validationResult.message}")
                AddWebServerResult.Fail(validationResult.message)
            }
        }
    }

    fun removeWebServerById(id: UUID): Boolean {
        val entity = repository.findById(id)
        entity.ifPresent {
            repository.delete(it)
        }
        if (entity.isPresent) {
            logger.debug("Deleting webserver check: ${entity.get().url}")
            repository.delete(entity.get())
            historyRepository.deleteByWebServerCheckId(id)
        } else {
            logger.warn("No webserver check with id $id was found")
        }
        return entity.isPresent
    }

    fun getAll(): List<WebServerCheck> {
        return repository.findAll().map { it.asDomain() }
    }

    fun getStatusForWebServer(id: UUID): WebServerCheckHistoryEntry? {
        return historyRepository.findFirstByWebServerCheckIdOrderByTimeStampDesc(id)
            .map {
                it.asDomain()
            }.getOrNull()
    }

    fun getHistoryForWebServerBetweenTimestamps(id: UUID, from: Instant, to: Instant): List<WebServerCheckHistoryEntry> {
        return historyRepository.findByTimeStampBetweenAndWebServerCheckIdIs(from, to, id)
            .map {
                it.asDomain()
            }
    }

    private fun checkAvailabilities() {
        repository.findAll()
            .forEach {
                logger.debug("Checking availability: ${it.url}")
                checkAvailable(it)
            }
    }

    private fun purgeOutOfDateEntries() {
        val maxAge = Instant.now()
            .minus(purgeConfig.olderThan, purgeConfig.unit)
        val deletedCount = historyRepository.deleteByTimeStampBefore(maxAge)
        logger.debug("Purged $deletedCount history older than {}", maxAge)
    }

    private fun checkAvailable(entity: WebServerCheckEntity) {
        val request: Request = Request.Builder()
            .url(entity.url)
            .build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                logger.debug("Request ${if (response.isSuccessful) "SUCCESS" else "FAIL"} ${entity.url}: ${response.code} - ${response.message}")
                val entity = WebServerCheckHistoryEntity(
                    UUID.randomUUID(),
                    entity.id,
                    Instant.now(),
                    response.code,
                    response.message,
                    if (response.isSuccessful) response.readLimitedBody() else null
                )
                historyRepository.save(
                    entity
                )
            }

            override fun onFailure(call: Call, e: IOException) {
                val errorMessage = e.message ?: e::class.java.simpleName
                logger.warn("Request FAIL ${entity.url}: $errorMessage")
                val entity = WebServerCheckHistoryEntity(
                    UUID.randomUUID(),
                    entity.id,
                    Instant.now(),
                    -1,
                    errorMessage,
                    null
                )
                historyRepository.save(
                    entity
                )
            }
        })
    }

    private fun Response.readLimitedBody() = try {
        peekBody(256).string()
    } catch (exception: Exception) {
        "${exception::class.simpleName}: ${exception.message}"
    }

    fun WebServerCheckHistoryEntity.asDomain(): WebServerCheckHistoryEntry {
        return with(this) {
            WebServerCheckHistoryEntry(id, webServerCheckId, timeStamp, responseCode, message, errorBody)
        }
    }

    private fun WebServerCheckEntity.asDomain(): WebServerCheck {
        return with(this) {
            WebServerCheck(id, url)
        }
    }

    sealed interface URLValidationResult {
        data class Invalid(val message: String) : URLValidationResult
        data object Valid : URLValidationResult
    }

    fun validateUrl(url: String): URLValidationResult {
        try {
            URL(url).toURI()
            return URLValidationResult.Valid
        } catch (e: MalformedURLException) {
            return URLValidationResult.Invalid(e.message ?: e::class.java.simpleName)
        } catch (e: URISyntaxException) {
            return URLValidationResult.Invalid(e.message ?: e::class.java.simpleName)
        }
    }
}
