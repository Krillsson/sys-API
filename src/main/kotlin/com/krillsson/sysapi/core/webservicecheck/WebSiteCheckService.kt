package com.krillsson.sysapi.core.webservicecheck

import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.util.logger
import okhttp3.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.jvm.optionals.getOrNull

@Service
class WebServerCheckService(
        private val repository: WebServerCheckRepository,
        yamlConfigFile: YAMLConfigFile,
        private val historyRepository: WebServerHistoryEntryRepository
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

    fun addWebServer(url: String) {
        repository.save(WebServerCheckEntity(UUID.randomUUID(), url))
    }

    fun removeWebServerById(id: UUID) {
        repository.deleteById(id)
        historyRepository.deleteByWebServerCheckId(id)
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
        logger.info("Purged $deletedCount history older than {}", maxAge)
    }

    private fun checkAvailable(entity: WebServerCheckEntity) {
        val request: Request = Request.Builder()
                .url(entity.url)
                .build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                logger.debug("Request ${if (response.isSuccessful) "SUCCESS" else "FAIL"} ${entity.url}: ${response.code} - ${response.message}")
                historyRepository.save(
                        WebServerCheckHistoryEntity(
                                UUID.randomUUID(),
                                entity.id,
                                Instant.now(),
                                response.code,
                                response.message,
                                if (response.isSuccessful) response.readLimitedBody() else null
                        )
                )
            }

            override fun onFailure(call: Call, e: IOException) {
                val errorMessage = e.message ?: e::class.java.simpleName
                logger.warn("Request FAIL ${entity.url}: $errorMessage")
                historyRepository.save(
                        WebServerCheckHistoryEntity(
                                UUID.randomUUID(),
                                entity.id,
                                Instant.now(),
                                -1,
                                errorMessage,
                                null
                        ))
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
        return with(this){
            WebServerCheck(id, url)
        }
    }
}
